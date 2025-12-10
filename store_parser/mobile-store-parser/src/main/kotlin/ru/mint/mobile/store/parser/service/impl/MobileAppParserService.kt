package ru.mint.mobile.store.parser.service.impl

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.fasterxml.jackson.databind.ObjectMapper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.mint.luminati.support.LuminatiHttpProxyClient
import ru.mint.mobile.store.parser.repository.entity.Application
import ru.mint.mobile.store.parser.repository.entity.ApplicationParsingData
import ru.mint.mobile.store.parser.repository.entity.ApplicationStatus
import ru.mint.mobile.store.parser.repository.entity.Platform
import ru.mint.mobile.store.parser.service.dto.AppParsingResult
import ru.mint.mobile.store.parser.service.AppParserService
import ru.mint.mobile.store.parser.service.AppSettingsService
import ru.mint.mobile.store.parser.service.dto.AppParsingResultWrapper
import ru.mint.mobile.store.parser.service.dto.FullAppData
import java.time.LocalDateTime
import java.util.*

@Service
class MobileAppParserService : AppParserService {

    @Autowired
    private lateinit var httpProxyClient: LuminatiHttpProxyClient<String>
    @Autowired
    private lateinit var appSettingsService: AppSettingsService
    @Autowired
    private lateinit var jacksonMapper: ObjectMapper

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(MobileAppParserService::class.java)
    }

    override fun parse(appId: String, platform: Platform, fastParse: Boolean): AppParsingResultWrapper {
        val parser = Parser()
        var appUrl = ""
        var title = ""
        var iconUrl = ""
        val fullAppDataText: String
        var parsed = false
        var hasNetworkError = false
        val fullAppData: FullAppData
        var reviews: List<String> = listOf()
        var screenshots: MutableList<String> = mutableListOf()
        val descrToLangs = mutableMapOf<String, MutableList<String>>()
        val docUrlTemplate = when (platform) {
            Platform.IOS -> appSettingsService.getITunesAppUrlTemplate()
            Platform.ANDROID -> appSettingsService.getAndroidAppUrlTemplate()
        }

        fun appParse(countryCode: String): Boolean {
            hasNetworkError = false
            val docUrl = when (platform) {
                Platform.IOS -> String.format(docUrlTemplate, countryCode, appId)
                Platform.ANDROID -> String.format(docUrlTemplate, appId, countryCode)
            }
            try {
                logger.debug("===>>> Try get data for $appId with country code: $countryCode")
                val response = httpProxyClient.get(docUrl)
                if (response.statusCode == 200) {
                    logger.debug("===>>> Get data for $appId with country code: $countryCode")
                    val doc = Jsoup.parse(response.body)
                    when (platform) {
                        Platform.ANDROID -> {
                            //Android app description
                            doc.select(appSettingsService.getTopParserAndroidDescriptionKey()).first()?.let {
                                val description = it.text()
                                val locale = Locale("", countryCode)
                                if (descrToLangs[description] != null && !fastParse) {
                                    descrToLangs[description]!!.add(locale.getDisplayCountry(Locale.ENGLISH))
                                } else {
                                    descrToLangs.put(description, mutableListOf(locale.getDisplayCountry(Locale.ENGLISH)))
                                }
                            }

                            //Anroid app reviews
                            reviews = GetAndroidReviewsBlock(doc)

                            //Android app screenshots
                            val screenImages = doc.select(appSettingsService.getTopParserAndroidScreenshotsKey())
                            if (screenImages.isNotEmpty() && screenshots.isEmpty()) {
                                screenImages.forEach { element ->
                                    screenshots.add(element.attr("src"))
                                }
                            }

                            if (appUrl.isEmpty()) {
                                appUrl = String.format(docUrlTemplate, appId, countryCode)

                                //Android app title
                                title = doc.select(appSettingsService.getTopParserAndroidTitleKey()).first().text()

                                //Android app icon
                                iconUrl = doc.select(appSettingsService.getTopParserAndroidIconUrlKey()).first().attr("src")
                            }

                            return true
                        }
                        Platform.IOS -> {
                            doc.select("script[type=application/ld+json]").first()?.data().let {
                                val map = (parser.parse(StringBuilder(it)) as JsonObject).map
                                val description = map["description"].toString()
                                val locale = Locale("", countryCode)
                                if (descrToLangs[description] != null && !fastParse) {
                                    descrToLangs[description]?.add(locale.getDisplayCountry(Locale.ENGLISH))
                                } else {
                                    descrToLangs.put(description, mutableListOf(locale.getDisplayCountry(Locale.ENGLISH)))
                                }

                                if (appUrl.isEmpty()) {
                                    appUrl = String.format(docUrlTemplate, countryCode, appId)
                                    title = map["name"] as String
                                    iconUrl = map["image"] as String
                                    if (screenshots.isEmpty()) screenshots = map["screenshot"] as MutableList<String>
                                }
                            }
                            return true
                        }
                    }
                } else {
                    return false
                }
            } catch (e: Exception) {
                httpProxyClient.resetFailureCountAndReinitProxy()
                logger.debug("===>>> appParse error: ${e.message}")
                hasNetworkError = true
            }
            return false
        }

        if (fastParse) parsed = appParse("RU")

        if (!fastParse || !parsed) {
            for (countryCode in Locale.getISOCountries()) {
                parsed = appParse(countryCode)

                if (fastParse && parsed) break
            }
        }

        fullAppData = FullAppData(reviews = reviews, screenShots = screenshots)
        fullAppDataText = jacksonMapper.writeValueAsString(fullAppData)

        return if (appUrl.isNotEmpty()) AppParsingResultWrapper(appParsingResult = AppParsingResult(appUrl, title, iconUrl, descrToLangs, fullAppDataText), hasError = false)
        else AppParsingResultWrapper(appParsingResult = null, hasError = hasNetworkError)
    }

    override fun androidAppPageParse(doc: Document, appId: String): Application {
        val fullAppDataText: String
        val fullAppData: FullAppData
        val screenshots: MutableList<String> = mutableListOf()
        val descrToLangs = mutableMapOf<String, MutableList<String>>()
        val docUrlTemplate = appSettingsService.getAndroidAppUrlTemplate()

        logger.debug("===>>> Get document data for $appId")

        //Android app description
        doc.select(appSettingsService.getTopParserAndroidDescriptionKey()).first()?.let {
            val locale = Locale("", "RU")
            descrToLangs.put(it.text(), mutableListOf(locale.getDisplayCountry(Locale.ENGLISH)))
        }

        //Android app reviews
        var reviews = GetAndroidReviewsBlock(doc)


        //Android app screenshots
        val screenImages = doc.select(appSettingsService.getTopParserAndroidScreenshotsKey())
        if (screenImages.isNotEmpty() && screenshots.isEmpty()) {
            screenImages.forEach { element ->
                screenshots.add(element.attr("src"))
            }
        }

        val appUrl = String.format(docUrlTemplate, appId, "RU")

        //Android app title
        val title = doc.select(appSettingsService.getTopParserAndroidTitleKey()).first().text()

        //Android app icon
        val iconUrl = doc.select(appSettingsService.getTopParserAndroidIconUrlKey()).first().attr("src")


        fullAppData = FullAppData(reviews = reviews, screenShots = screenshots)
        fullAppDataText = jacksonMapper.writeValueAsString(fullAppData)

        val application = Application(appId = appId, platform = Platform.ANDROID, status = ApplicationStatus.AVAILABLE, creationDate = LocalDateTime.now(), title = title, url = appUrl, iconUrl = iconUrl, fullData = fullAppDataText)
        val appParsingData = ApplicationParsingData(application, jacksonMapper.writeValueAsString(descrToLangs))
        application.parsingDataList = mutableListOf(appParsingData)
        return application
    }

    override fun iosAppPageParse(doc: Document, appId: String): Application {
        val parser = Parser()
        var fullAppData: FullAppData
        var fullAppDataText = ""
        var title = ""
        var iconUrl = ""
        val reviews: MutableList<String> = mutableListOf()
        val descrToLangs = mutableMapOf<String, MutableList<String>>()
        val docUrlTemplate = appSettingsService.getITunesAppUrlTemplate()
        val appUrl = String.format(docUrlTemplate, "RU", appId)

        doc.select("script[type=application/ld+json]").first()?.data().let {
            val map = (parser.parse(StringBuilder(it)) as JsonObject).map
            val description = map["description"].toString()
            val locale = Locale("", "RU")
            descrToLangs.put(description, mutableListOf(locale.getDisplayCountry(Locale.ENGLISH)))
            title = map["name"] as String
            iconUrl = map["image"] as String
            val screenshots = map["screenshot"] as MutableList<String>
            fullAppData = FullAppData(reviews = reviews, screenShots = screenshots)
            fullAppDataText = jacksonMapper.writeValueAsString(fullAppData)
        }

        val application = Application(appId = appId, platform = Platform.IOS, status = ApplicationStatus.AVAILABLE, creationDate = LocalDateTime.now(), title = title, url = appUrl, iconUrl = iconUrl, fullData = fullAppDataText)
        val appParsingData = ApplicationParsingData(application, jacksonMapper.writeValueAsString(descrToLangs))
        application.parsingDataList = mutableListOf(appParsingData)
        return application
    }

    private fun GetAndroidReviewsBlock(doc: Document): List<String> {
        val parser = Parser()
        var result = mutableListOf<String>()
        try {
            var scripts = doc.getElementsByTag("script")
            for (script in scripts) {
                if (script != null) {
                    if (script.html().indexOf(appSettingsService.getTopParserAndroidReviewsKey()) > -1) {
                        var complete=script.html()
                        var startJSON=complete.indexOf('[')
                        var endJSON=complete.lastIndexOf(']') + 1
                        var json = complete.substring(startJSON, endJSON).replace("null,", "", ignoreCase = true)
                        var reviewsArray = parser.parse(StringBuilder(json)) as JsonArray<JsonArray<JsonArray<Any>>>
                        if (reviewsArray.size > 0) {
                            for (reviewObject in reviewsArray[0]) {
                                if (reviewObject.size > 3) {
                                    result.add(reviewObject[3].toString())
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (e: Exception) {
            return result
        }
        return result
    }
}