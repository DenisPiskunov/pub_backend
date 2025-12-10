package ru.mint.mobile.store.parser.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.mint.mobile.store.parser.repository.ApplicationRepository
import ru.mint.mobile.store.parser.repository.AppsCategoriesRepository
import ru.mint.mobile.store.parser.repository.entity.*
import ru.mint.mobile.store.parser.service.*
import ru.mint.mobile.store.parser.service.dto.ApplicationState
import ru.mint.mobile.store.parser.service.dto.ApplicationStateEmailDetails
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Service
class ApplicationParsingDataServiceImpl : ApplicationParsingDataService {

    @Autowired
    private lateinit var applicationRepository: ApplicationRepository

    @Autowired
    private lateinit var appParserService: AppParserService
    @Autowired
    private lateinit var emailSenderService: EmailSenderService
    @Autowired
    private lateinit var jacksonMapper: ObjectMapper
    @Autowired
    private lateinit var appSettingsService: AppSettingsService
    @Autowired
    private lateinit var topProductParserService: TopProductParserService
    @Autowired
    private lateinit var appsCategoriesRepository: AppsCategoriesRepository

    private var applicationQueue = ApplicationQueue.getInstance()
    private val emailDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")


    private var queueFinished: Boolean = true
    private var emails = mutableListOf<ApplicationStateEmailDetails>()

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(ApplicationParsingDataServiceImpl::class.java)
    }
    override fun processApplication(application: Application, fastParse: Boolean): Application? {
        logger.debug("Start to parse and process an application with id = ${application.appId}")
        val parsingResult = appParserService.parse(application.appId, application.platform, fastParse)
        if (parsingResult.appParsingResult != null) {
            val appParsingData = ApplicationParsingData(application, jacksonMapper.writeValueAsString(parsingResult.appParsingResult.descrToLangs))
            application.fullData = parsingResult.appParsingResult.fullData
//                means that parsing for specified application executed first time
            if (application.url == null) {
                application.url = parsingResult.appParsingResult.appUrl
                application.title = parsingResult.appParsingResult.title
                application.iconUrl = parsingResult.appParsingResult.iconUrl
                application.parsingDataList = mutableListOf(appParsingData)
                logger.debug("Data for an application with id = ${application.appId} was created.")

                val descrToLangsForMail = mutableMapOf<String, List<String>>()
                parsingResult.appParsingResult.descrToLangs.forEach { item ->
                    val shortDescription = item.key.split(".", "!")[0]
                    descrToLangsForMail.put(shortDescription, item.value)
                }
                emails.add(ApplicationStateEmailDetails.create(application.title!!, application.url!!, application.iconUrl!!, ApplicationState.NEW, descrToLangsForMail, application.keyword, application.creationDate))
            } else {
                if (application.parsingDataList.count() > 0) {
                    application.parsingDataList.sortBy { parsingData -> parsingData.creationDate }
                    val lastParsingData = application.parsingDataList.last()
                    val lastDescrToLangs = jacksonMapper.readValue<Map<String, List<String>>>(lastParsingData.parsingResult, object : TypeReference<Map<String, List<String>>>() {})
                    if (isApplicationDataChanged(lastDescrToLangs, parsingResult.appParsingResult.descrToLangs)) {
                        logger.debug("Detected changes for an application with id = ${application.appId}.")
                        application.parsingDataList.add(appParsingData)
//                        for cases when application was UNAVAILABLE then became AVAILABLE
                        application.status = ApplicationStatus.AVAILABLE

                        val descrToLangsForMail = mutableMapOf<String, List<String>>()
                        parsingResult.appParsingResult.descrToLangs.forEach { item ->
                            val shortDescription = item.key.split(".", "!")[0]
                            descrToLangsForMail.put(shortDescription, item.value)
                        }
                        emails.add(ApplicationStateEmailDetails.create(application.title!!, application.url!!, application.iconUrl!!, null, descrToLangsForMail, application.keyword, application.creationDate))
                    } else {
                        if (application.status == ApplicationStatus.AVAILABLE) {
                            logger.debug("An application with id = ${application.appId} was not changed.")
                            return null
                        } else {
                            logger.debug("An application with id = ${application.appId} is ${ApplicationStatus.AVAILABLE} now.")
                            application.status = ApplicationStatus.AVAILABLE
                            val descrToLangsForMail = mutableMapOf<String, List<String>>()
                            lastDescrToLangs.forEach { item ->
                                val shortDescription = item.key.split(".", "!")[0]
                                descrToLangsForMail.put(shortDescription, item.value)
                            }
                            emails.add(ApplicationStateEmailDetails.create(application.title!!, application.url!!, application.iconUrl!!, ApplicationState.AVAILABLE, descrToLangsForMail, application.keyword, application.creationDate))
                        }
                    }
                } else {
                    application.parsingDataList.add(appParsingData)
                    val descrToLangs = jacksonMapper.readValue<Map<String, List<String>>>(appParsingData.parsingResult, object : TypeReference<Map<String, List<String>>>() {})
                    val descrToLangsForMail = mutableMapOf<String, List<String>>()
                    descrToLangs.forEach { item ->
                        val shortDescription = item.key.split(".", "!")[0]
                        descrToLangsForMail.put(shortDescription, item.value)
                    }
                    emails.add(ApplicationStateEmailDetails.create(application.title!!, application.url!!, application.iconUrl!!, ApplicationState.AVAILABLE, descrToLangsForMail, application.keyword, application.creationDate))
                }
            }

        } else {
            if (!parsingResult.hasError) {
                var lastDescrToLangs =mapOf<String, List<String>>()
                logger.debug("An application with id = ${application.appId} is unavailable")
                if (application.title.isNullOrEmpty()) {
                    application.title = String.format("Application ID: %s", application.appId)
                }
                if (application.iconUrl.isNullOrEmpty()) {
                    application.iconUrl = appSettingsService.getRemovedAppIconUrl()
                }
                application.status = ApplicationStatus.UNAVAILABLE
                if (application.deletedDate == null) {
                    application.deletedDate = LocalDateTime.now()
                }
                application.url = when (application.platform) {
                    Platform.IOS -> {
                        val iTunesAppUrlTemplateSimple = appSettingsService.getITunesAppUrlTemplateSimple()
                        String.format(iTunesAppUrlTemplateSimple, application.appId)
                    }
                    Platform.ANDROID -> {
                        val androidAppUrlTemplateSimple = appSettingsService.getAndroidAppUrlTemplateSimple()
                        String.format(androidAppUrlTemplateSimple, application.appId)
                    }
                }
                if (application.parsingDataList.count() > 0) {
                    application.parsingDataList.sortBy { parsingData -> parsingData.creationDate }
                    val lastParsingData = application.parsingDataList.last()
                    lastDescrToLangs = jacksonMapper.readValue<Map<String, List<String>>>(lastParsingData.parsingResult, object : TypeReference<Map<String, List<String>>>() {})
                }
                val descrToLangsForMail = mutableMapOf<String, List<String>>()
                lastDescrToLangs.forEach { item ->
                    val shortDescription = item.key.split(".", "!")[0]
                    descrToLangsForMail.put(shortDescription, item.value)
                }
                emails.add(ApplicationStateEmailDetails.create(application.title!!, application.url!!, application.iconUrl!!, ApplicationState.REMOVED, descrToLangsForMail, application.keyword, application.creationDate))
            } else {
                return null
            }
        }
        logger.debug("Parsing/processing of an application with id = ${application.appId} finished.")
        return application
    }

    private fun isApplicationDataChanged(lastDescrToLangs: Map<String, List<String>>, newDescrToLangs: Map<String, List<String>>): Boolean {

        fun equals(collection1: Collection<*>, collection2: Collection<*>): Boolean {
            if (collection1.size != collection2.size) {
                return false
            }
            if (collection1.size != collection1.intersect(collection2).size) {
                return false
            }
            return true
        }

        if (!equals(lastDescrToLangs.keys, newDescrToLangs.keys)) {
            return true
        }
        for ((description, langs) in lastDescrToLangs) {
            if (!equals(langs, newDescrToLangs[description]!!)) {
                return true
            }
        }
        return false
    }

    override fun searchInQueue() {
        if (queueFinished) {
            while (applicationQueue.count() > 0) {
                queueFinished = false
                val application = applicationQueue.dequeue()!!
                if (!applicationRepository.existsByAppId(appId = application.appId)){
                    val processedApplication = processApplication(application = application, fastParse = true)
                    if (processedApplication != null) {
                        applicationRepository.save(processedApplication)
                    }
                }
            }
            queueFinished = true
        }
    }

    override fun processNewTopApplications() {
        var allTopApplications = mutableListOf<Application>()
        var currentTopSelection = mutableListOf<Application>()
        var androidAppsCategories: List<AppsCategories> = appsCategoriesRepository.findByPlatformAndSearch("android", true)
        var iosAppsCategories: List<AppsCategories> = appsCategoriesRepository.findByPlatformAndSearch("ios", true)

        androidAppsCategories.forEach {
            var categoryKey = it.key
            var category = it.name

            currentTopSelection.clear()
            currentTopSelection = topProductParserService.parseTopAndroidByCategories(categoryKey, category)
            if (currentTopSelection.isNotEmpty()) {
                applicationRepository.save(currentTopSelection)
                allTopApplications.addAll(currentTopSelection)
            }
        }

        iosAppsCategories.forEach {
            var categoryKey = it.key
            var category = it.name

            currentTopSelection.clear()
            currentTopSelection = topProductParserService.parseTopIOSByCategories(categoryKey, category)
            if (currentTopSelection.isNotEmpty()) {
                applicationRepository.save(currentTopSelection)
                allTopApplications.addAll(currentTopSelection)
            }
        }

        allTopApplications.forEach { application ->
            val descrToLangsForMail = mutableMapOf<String, List<String>>()
            var lastDescrToLangs =mapOf<String, List<String>>()
            if (application.parsingDataList.count() > 0) {
                application.parsingDataList.sortBy { parsingData -> parsingData.creationDate }
                val lastParsingData = application.parsingDataList.last()
                lastDescrToLangs = jacksonMapper.readValue<Map<String, List<String>>>(lastParsingData.parsingResult, object : TypeReference<Map<String, List<String>>>() {})
            }
            lastDescrToLangs.forEach { item ->
                val shortDescription = item.key.split(".", "!")[0]
                descrToLangsForMail.put(shortDescription, item.value)
            }

            application.parsingDataList.last().parsingResult
            emails.add(ApplicationStateEmailDetails.create(application.title!!, application.url!!, application.iconUrl!!, ApplicationState.NEW, descrToLangsForMail, application.keyword, application.creationDate))
        }
        emailSenderService.sendAppsStateInfo(emails, "Daily statistic: ${LocalDateTime.now().format(emailDateFormat)}")
        emails.clear()
    }

    override fun updateParsingData() {
        val selectedApplications = applicationRepository.findDistinctFetchAppParsingDataByStatusInAndValid(listOf(ApplicationStatus.AVAILABLE), true)
        prepareAndSaveApplicationList(selectedApplications)
    }

    override fun prepareAndSaveApplicationList(applications: List<Application>, fastParse: Boolean) {
        val changedApplications = mutableListOf<Application>()
        logger.debug("Found ${applications.size} applications to process.")
        applications.forEach { application ->
            val processedApplication = processApplication(application, fastParse)
            if (processedApplication != null) {
                changedApplications.add(processedApplication)
            }
        }
        if (changedApplications.isNotEmpty()) {
            applicationRepository.save(changedApplications)
        }
        emailSenderService.sendAppsStateInfo(emails, "Daily statistic: ${LocalDateTime.now().format(emailDateFormat)}")
        emails.clear()
    }

}