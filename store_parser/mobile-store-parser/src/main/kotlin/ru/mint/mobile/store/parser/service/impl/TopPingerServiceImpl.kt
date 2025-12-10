package ru.mint.mobile.store.parser.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.mint.luminati.support.LuminatiHttpProxyClient
import ru.mint.mobile.store.parser.repository.ApplicationRepository
import ru.mint.mobile.store.parser.repository.entity.Application
import ru.mint.mobile.store.parser.repository.entity.ApplicationStatus
import ru.mint.mobile.store.parser.repository.entity.Platform
import ru.mint.mobile.store.parser.service.AppSettingsService
import ru.mint.mobile.store.parser.service.EmailSenderService
import ru.mint.mobile.store.parser.service.TopPingerService
import ru.mint.mobile.store.parser.service.dto.ApplicationState
import ru.mint.mobile.store.parser.service.dto.ApplicationStateEmailDetails
import ru.mint.mobile.store.parser.service.dto.MailAppStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class TopPingerServiceImpl : TopPingerService {

    @Autowired
    private lateinit var httpProxyClient: LuminatiHttpProxyClient<String>
    @Autowired
    private lateinit var appSettingsService: AppSettingsService
    @Autowired
    private lateinit var applicationRepository: ApplicationRepository
    @Autowired
    private lateinit var emailSenderService: EmailSenderService
    @Autowired
    private lateinit var jacksonMapper: ObjectMapper
    private var emails = mutableListOf<ApplicationStateEmailDetails>()
    private val emailDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(TopPingerService::class.java)
    }

    override fun pingApps() {
        val androidAppUrlTemplate = appSettingsService.getAndroidAppUrlTemplateSimple()
        val iosAppUrlTemplate = appSettingsService.getITunesAppUrlTemplateSimple()
        val applications = applicationRepository.findDistinctFetchAppParsingDataByStatusInAndValid(listOf(ApplicationStatus.AVAILABLE), true)
        val deletedApplications = mutableListOf<Application>()

        emails.clear()
        applications.forEach { application ->
            val appId = application.appId
            logger.debug("===>>> Ping $appId")
            val appUrl = when (application.platform) {
                Platform.ANDROID -> {
                    String.format(androidAppUrlTemplate, appId)
                }
                Platform.IOS -> {
                    String.format(iosAppUrlTemplate, appId)
                }
            }
            try {
                val responseTop = httpProxyClient.get(appUrl)
                if (responseTop.statusCode == 200) {
                    if (application.platform == Platform.IOS) {
                        val doc = Jsoup.parse(responseTop.body)
                        val title = doc.select("title").first().text()
                        if (title.contains("iTunes Store")) {
                            logger.debug("===>>> $appId are removed")
                            prepareRemovedApp(application)
                            deletedApplications.add(application)
                        } else {
                            logger.debug("===>>> $appId not removed")
                        }

                    } else {
                        logger.debug("===>>> $appId not removed")
                    }
                }
                else {
                    logger.debug("===>>> $appId are removed")
                    prepareRemovedApp(application)
                    deletedApplications.add(application)
                }
            } catch (e: Exception) {
                logger.debug("===>>> Error getting $appId. Error [${e.message}]")
                httpProxyClient.resetFailureCountAndReinitProxy()
            }
        }
        if (deletedApplications.isNotEmpty()) {
            applicationRepository.save(deletedApplications)
        }
        emailSenderService.sendAppsStateInfo(emails, "Runtime statistic. Removed apps: ${LocalDateTime.now().format(emailDateFormat)}", MailAppStatus.REMOVED)
    }

    override fun prepareRemovedApp(application: Application) {
        application.status = ApplicationStatus.UNAVAILABLE
        application.deletedDate = LocalDateTime.now()

        val lastDescrToLangs = if (application.parsingDataList.count() > 0) {
            application.parsingDataList.sortBy { parsingData -> parsingData.creationDate }
            val lastParsingData = application.parsingDataList.last()
            jacksonMapper.readValue<Map<String, List<String>>>(lastParsingData.parsingResult, object : TypeReference<Map<String, List<String>>>() {})
        } else {
            mapOf()
        }


        val lastDescrToLangsForMail = mutableMapOf<String, List<String>>()
        var i = 0

        lastDescrToLangs.forEach { item ->
            val shortDescription = item.key.split(".", "!")[0]
            if (item.value.contains("Russia")) {
                lastDescrToLangsForMail.put(shortDescription, item.value)
                return@forEach
            }
            if (i.inc() == lastDescrToLangs.size - 1) {
                lastDescrToLangsForMail.put(shortDescription, item.value)
            }

        }
        emails.add(ApplicationStateEmailDetails.create(application.title!!, application.url!!, application.iconUrl!!, ApplicationState.REMOVED, lastDescrToLangsForMail, application.keyword, application.creationDate))
    }
}