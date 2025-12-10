package ru.mint.mobile.store.parser.service.impl

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.mint.luminati.support.LuminatiHttpProxyClient
import ru.mint.mobile.store.parser.repository.AppKeyGamblingRepository
import ru.mint.mobile.store.parser.repository.ApplicationRepository
import ru.mint.mobile.store.parser.repository.ExcludedWordsRepository
import ru.mint.mobile.store.parser.repository.entity.*
import ru.mint.mobile.store.parser.service.AppParserService
import ru.mint.mobile.store.parser.service.AppSettingsService
import ru.mint.mobile.store.parser.service.EmailSenderService
import ru.mint.mobile.store.parser.service.TopProductParserService

@Service
class TopProductParserServiceImpl : TopProductParserService {

    @Autowired
    private lateinit var httpProxyClient: LuminatiHttpProxyClient<String>
    @Autowired
    private lateinit var appSettingsService: AppSettingsService
    @Autowired
    private lateinit var excludeProductRepository: ExcludedWordsRepository
    @Autowired
    private lateinit var appKeyGamblingRepository: AppKeyGamblingRepository
    @Autowired
    private lateinit var applicationRepository: ApplicationRepository
    @Autowired
    private lateinit var mobileAppParserService: AppParserService
    @Autowired
    private lateinit var emailSenderService: EmailSenderService


    private var gamblingKeysList: List<String> = listOf()
    private var excludeWordList: List<String> = listOf()
    private var excludedWordsPattern: String = ""
    private var gamblingKeysPattern: String = ""
    private var androidTopURL: String = ""
    private var androidAppUrlTemplate: String = ""
    private var iosTopURL = ""
    private var iosAppUrlTemplate = ""
    private var excludedWordsRegex: Regex? = null
    private var gamblingKeysRegex: Regex? = null
    private var isInialized = false

    override fun preinitParser() {
        gamblingKeysList = appKeyGamblingRepository.findAll().map { it.key }.toList()
        excludeWordList = excludeProductRepository.findAll().map { it.key }.toList()
        var searchRegexTemplate = appSettingsService.getWordsSearchRegex()

        excludeWordList.forEach {word ->
            val lastWord = excludeWordList.last()
            val searchRegex = searchRegexTemplate.replace("%s", word)
            excludedWordsPattern = if (word != lastWord) {
                "$excludedWordsPattern$searchRegex|"
            } else {
                "$excludedWordsPattern$searchRegex"
            }

        }

        gamblingKeysList.forEach {word ->
            val lastWord = gamblingKeysList.last()
            val searchRegex = searchRegexTemplate.replace("%s", word)
            gamblingKeysPattern = if (word != lastWord) {
                "$gamblingKeysPattern$searchRegex|"
            } else {
                "$gamblingKeysPattern$searchRegex"
            }
        }

        excludedWordsRegex = Regex(excludedWordsPattern, setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
        gamblingKeysRegex = Regex(gamblingKeysPattern, setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
        androidTopURL = appSettingsService.getAndroidTopUrl()
        androidAppUrlTemplate = appSettingsService.getAndroidAppUrlTemplate()
        iosTopURL = appSettingsService.getITunesTopUrl()
        iosAppUrlTemplate = appSettingsService.getITunesAppUrlTemplate()
        isInialized = true
    }

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(TopProductParserService::class.java)
    }

//    override fun parseTop(): List<Application> {
//        val parser = Parser()
//        var androidAppId: String
//        var androidAppUrl: String
//
//        var exclude: Boolean
//        var exist: Boolean
//        val applicationsList = mutableListOf<Application>()
//        val androidAppsUrlList = mutableListOf<String>()
//
//        try {
//            logger.debug("===>>> Start Android TOP-parsing")
//            var appExampleSended = false
//            val url = String.format(androidTopURL, "0")
//            val docTop = if (appSettingsService.getUseLuminati()) {
//                val docTopResponse =  httpProxyClient.get(url)
//                Jsoup.parse(docTopResponse.body)
//            } else {
//                Jsoup.connect(url).get()
//            }
//            val cards = docTop.select(appSettingsService.getTopParserAndroidCardsKey())
//
//            for (card in cards) {
//                androidAppId = card.attr(appSettingsService.detTopParserAndroidAppIDInCard())
//                if (applicationRepository.existsByAppId(androidAppId)) {
//                    continue
//                }
//                androidAppUrl = String.format(androidAppUrlTemplate, androidAppId, "ru")
//                androidAppsUrlList.add(androidAppUrl)
//                val response = httpProxyClient.get(androidAppUrl)
//                val doc = Jsoup.parse(response.body)
//                if (!appExampleSended) {
//                    emailSenderService.sendMessage(doc.toString(), "Android application example")
//                    appExampleSended = true
//                }
//                val appDescription = doc.select(appSettingsService.getTopParserAndroidMainContainerKey()).first().text()
//                val reviews = GetAndroidReviewsBlock(doc)
//                val mainContent = appDescription.plus(reviews)
//
//                exclude = excludedWordsRegex.containsMatchIn(appDescription)
//                if (exclude) {
//                    continue
//                }
//
//                exist = gamblingKeysRegex.containsMatchIn(mainContent)
//                if (exist) {
//                    val parsedApp = mobileAppParserService.androidAppPageParse(doc, androidAppId)
//                    parsedApp.keyword = gamblingKeysRegex.find(mainContent)!!.value
//                    applicationsList.add(parsedApp)
//                }
//
//            }
//            if (androidAppsUrlList.count() > 0) {
//                emailSenderService.sendMessage(androidAppsUrlList.joinToString(" ; \n"), "Android top applications list")
//            }
//            logger.debug("===>>> Stop Android TOP-parsing")
//        } catch (e: Exception) {
//            logger.debug("Android TOP parsing error: " + e.message)
//        }
//
//        //iOS
//        try {
//            logger.debug(("===>>> Start IOS TOP-Parsing"))
//            val appList = StringBuilder(Jsoup.connect(iosTopURL).get().body().text())
//            val feed = ((parser.parse(appList) as JsonObject).map)["feed"] as JsonObject
//            val entries = (feed.map)["entry"] as JsonArray<JsonObject>
//            for (entry in entries) {
//                iosEntryText = entry.toString()
//                exclude = excludedWordsRegex.containsMatchIn(iosEntryText)
//                if (exclude) {
//                    continue
//                }
//
//                exist = gamblingKeysRegex.containsMatchIn(iosEntryText)
//                if (exist) {
//                    iosAppId = (((entry.map)["id"] as JsonObject)["attributes"] as JsonObject)["im:id"].toString()
//                    if (!applicationRepository.existsByAppId(iosAppId)) {
//                        val iosAppUrl = String.format(iosAppUrlTemplate, "RU", iosAppId)
//                        val response = httpProxyClient.get(iosAppUrl)
//                        val doc = Jsoup.parse(response.body)
//                        val parsedApp = mobileAppParserService.iosAppPageParse(doc, iosAppId)
//                        parsedApp.keyword = gamblingKeysRegex.find(iosEntryText)!!.value;
//                        applicationsList.add(parsedApp)
//                    }
//                }
//            }
//            logger.debug("===>>> Stop IOS TOP-Parser")
//        } catch (e: Exception) {
//            logger.debug("iOS TOP parsing error: " + e.message)
//        }
//        return applicationsList
//    }

    override fun parseTopAndroidByCategories(categoryKey: String, category: String): MutableList<Application> {
        var androidAppId: String
        var androidAppUrl: String

        var exclude: Boolean
        var exist: Boolean
        val applicationsList = mutableListOf<Application>()
        val androidAppsUrlList = mutableListOf<String>()

        if (!isInialized) {
            preinitParser()
        }

        try {
            logger.debug("===>>> Start Android TOP-parsing")
            val url = String.format(androidTopURL, categoryKey, "0")
            val docTop = if (appSettingsService.getUseLuminati()) {
                val docTopResponse = httpProxyClient.get(url)
                Jsoup.parse(docTopResponse.body)
            } else {
                Jsoup.connect(url).get()
            }
            val cards = docTop.select(appSettingsService.getTopParserAndroidCardsKey())

            for (card in cards) {
                androidAppId = card.attr(appSettingsService.detTopParserAndroidAppIDInCard())
                if (applicationRepository.existsByAppId(androidAppId)) {
                    continue
                }
                androidAppUrl = String.format(androidAppUrlTemplate, androidAppId, "ru")
                androidAppsUrlList.add(androidAppUrl)
                val response = httpProxyClient.get(androidAppUrl)
                val doc = Jsoup.parse(response.body)

                val appDescription = doc.select(appSettingsService.getTopParserAndroidMainContainerKey()).first().text()
                val reviews = GetAndroidReviewsBlock(doc)
                val mainContent = appDescription.plus(reviews)

                exclude = excludedWordsRegex!!.containsMatchIn(appDescription)
                if (exclude) {
                    continue
                }

                exist = gamblingKeysRegex!!.containsMatchIn(mainContent)
                if (exist) {
                    val parsedApp = mobileAppParserService.androidAppPageParse(doc, androidAppId)
                    parsedApp.keyword = gamblingKeysRegex!!.find(mainContent)!!.value
                    parsedApp.categoryKey = categoryKey
                    parsedApp.category = category
                    applicationsList.add(parsedApp)
                }

            }
            logger.debug("===>>> Stop Android TOP-parsing")
        } catch (e: Exception) {
            logger.debug("Android TOP parsing error: " + e.message)
        }
        return applicationsList
    }

    override fun parseTopIOSByCategories(categoryKey: String, category: String): MutableList<Application> {
        val parser = Parser()
        var iosEntryText: String
        var iosAppId: String
        var exclude: Boolean
        var exist: Boolean
        val applicationsList = mutableListOf<Application>()

        if (!isInialized) {
            preinitParser()
        }

        try {
            logger.debug(("===>>> Start IOS TOP-Parsing"))
            val url = String.format(iosTopURL, categoryKey)
            val appList = StringBuilder(Jsoup.connect(url).get().body().text())
            val feed = ((parser.parse(appList) as JsonObject).map)["feed"] as JsonObject
            val entries = (feed.map)["entry"] as JsonArray<JsonObject>
            for (entry in entries) {
                iosEntryText = entry.toString()
                exclude = excludedWordsRegex!!.containsMatchIn(iosEntryText)
                if (exclude) {
                    continue
                }

                exist = gamblingKeysRegex!!.containsMatchIn(iosEntryText)
                if (exist) {
                     iosAppId = (((entry.map)["id"] as JsonObject)["attributes"] as JsonObject)["im:id"].toString()
                    if (!applicationRepository.existsByAppId(iosAppId)) {
                        val iosAppUrl = String.format(iosAppUrlTemplate, "RU", iosAppId)
                        val response = httpProxyClient.get(iosAppUrl)
                        val doc = Jsoup.parse(response.body)
                        val parsedApp = mobileAppParserService.iosAppPageParse(doc, iosAppId)
                        parsedApp.keyword = gamblingKeysRegex!!.find(iosEntryText)!!.value
                        parsedApp.categoryKey = categoryKey
                        parsedApp.category = category
                        applicationsList.add(parsedApp)
                    }
                }
            }
            logger.debug("===>>> Stop IOS TOP-Parser")
        } catch (e: Exception) {
            logger.debug("iOS TOP parsing error: " + e.message)
        }
        return applicationsList
    }

    private fun GetAndroidReviewsBlock(doc: Document): String {
        var result = ""
        var scripts = doc.getElementsByTag("script")


        for (script in scripts) {
            if (script != null) {
                if (script.html().indexOf(appSettingsService.getTopParserAndroidReviewsKey()) > -1) {
                    var complete = script.html()
                    var startJSON = complete.indexOf('[') + 1
                    var endJSON = complete.lastIndexOf(']')
                    result = complete.substring(startJSON, endJSON)
                }
            }
        }
        return result
    }


}