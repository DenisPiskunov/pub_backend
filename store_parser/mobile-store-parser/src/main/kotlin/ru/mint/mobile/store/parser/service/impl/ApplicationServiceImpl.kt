package ru.mint.mobile.store.parser.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.experimental.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.mint.mobile.store.parser.repository.ApplicationRepository
import ru.mint.mobile.store.parser.repository.entity.Application
import ru.mint.mobile.store.parser.repository.entity.ApplicationStatus
import ru.mint.mobile.store.parser.repository.entity.Platform
import ru.mint.mobile.store.parser.rest.domain.NewAppData
import ru.mint.mobile.store.parser.service.ApplicationParsingDataService
import ru.mint.mobile.store.parser.service.ApplicationService
import ru.mint.mobile.store.parser.service.ApplicationQueue
import ru.mint.mobile.store.parser.service.dto.*
import java.lang.Math.round
import java.math.BigInteger
import java.sql.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Service
class ApplicationServiceImpl : ApplicationService {
    private val applicationQueue: ApplicationQueue = ApplicationQueue.getInstance()
    @Autowired
    private lateinit var applicationRepository: ApplicationRepository
    @Autowired
    private lateinit var applicationParsingDataService: ApplicationParsingDataService
    @Autowired
    private lateinit var jacksonMapper: ObjectMapper

    override fun createApp(appData: List<NewAppData>) {
        launch {
            appData.map { Application(it.appId, it.platform, ApplicationStatus.AVAILABLE, keyword = "manual_added") }.forEach { applicationQueue.enqueue(it) }
            applicationParsingDataService.searchInQueue()
        }
    }

    override fun createAppWithFilter(appData: List<NewAppData>) {
        createApp(appData.filter { !applicationRepository.existsByAppId(it.appId) })
    }

    override fun getAllApps(platform: String, status: String): List<ApplicationDetails> {
        val appsStatus: ApplicationStatus = try {
            ApplicationStatus.valueOf(status)
        } catch (e: Exception) {
            ApplicationStatus.AVAILABLE
        }
        val allApps = applicationRepository.findDistinctByPlatformAndStatusInAndValidOrderByCreationDateDesc(Platform.valueOf(platform.toUpperCase()), mutableListOf(appsStatus), true)
        val appDetailsList = mutableListOf<ApplicationDetails>()
        allApps.forEach { app ->
            appDetailsList.add(ApplicationDetails.create(app))
        }
        return appDetailsList
    }

    override fun getSingleAppData(appId: String): AppAdditionalData {
        if (appId.isBlank()) {
            return AppAdditionalData(listOf(AppDescription("", mutableListOf())), listOf(), listOf())
        }
        val appData = applicationRepository.findFetchApplicationByAppId(appId)
        val descriptionList: MutableList<AppDescription> = mutableListOf()
        var appReviews: MutableList<String>? = mutableListOf()
        var appScreeShots: MutableList<String>? = mutableListOf()
        if (appData.parsingDataList.isNotEmpty()) {
            val lastParsingResult = appData.parsingDataList.last().parsingResult
            val lastDescrToLangs = jacksonMapper.readValue<Map<String, MutableList<String>>>(lastParsingResult, object : TypeReference<Map<String, MutableList<String>>>() {})
            lastDescrToLangs.forEach { descriptionList.add(AppDescription(it.key, it.value)) }
        }

        if (!appData.fullData.isNullOrBlank()) {
            val fullAppData = jacksonMapper.readValue<Map<String, MutableList<String>>>(appData.fullData, object : TypeReference<Map<String, MutableList<String>>>() {})
            appReviews = fullAppData["reviews"]
            appScreeShots = fullAppData["screenShots"]
        }
        return AppAdditionalData(descriptionList, appReviews, appScreeShots)
    }

    override fun  getAppStatistics(fromDate: LocalDate, toDate: LocalDate): List<ApplicationAllStatData> {
        var applicationAllStatDataList = mutableListOf<ApplicationAllStatData>()
        val addingStatistics = prepareAppStatistics(applicationRepository.findFetchAddedApplications(), fromDate, toDate)
        val deletionStatistics = prepareAppStatistics(applicationRepository.findFetchApplicationByDeletedDateInterval(), fromDate, toDate)
        val activeStatistics = prepareAppStatistics(applicationRepository.findFetchActiveApplications(), fromDate, toDate)

        activeStatistics.forEach {
            var addedCount = 0
            var deletedCount = 0
            var percentage = "0%"

            val platform = it.platform
            val activeCount = it.count
            val statDate = it.date

            val addingData = addingStatistics.find {it.platform == platform && it.date == statDate}
            if (addingData != null) {
                addedCount = addingData.count
            }

            val deletionData = deletionStatistics.find {it.platform == platform && it.date == statDate}
            if (deletionData != null) {
                deletedCount = deletionData.count
            }

            if (activeCount + deletedCount + addedCount > 0) {
                percentage = "%d%%".format(round(deletedCount.toFloat() * 100 / (activeCount + deletedCount + addedCount)))
            }
            applicationAllStatDataList.add(ApplicationAllStatData(activeCount, addedCount, deletedCount, percentage, platform, statDate))
        }
        return applicationAllStatDataList
    }

//    override fun  getAppStatistics(fromDate: LocalDate, toDate: LocalDate): List<List<ApplicationStatData>> {
//        val addingStatistics = prepareAppStatistics(applicationRepository.findFetchAddedApplications(), fromDate, toDate)
//        val deletionStatistics = prepareAppStatistics(applicationRepository.findFetchApplicationByDeletedDateInterval(), fromDate, toDate)
//        val activeStatistics = prepareAppStatistics(applicationRepository.findFetchActiveApplications(), fromDate, toDate)
//
//        var appStatistic = mutableListOf<List<ApplicationStatData>>()
//        appStatistic.add(addingStatistics)
//        appStatistic.add(deletionStatistics)
//        appStatistic.add(activeStatistics)
//        return appStatistic
//    }

    private fun prepareAppStatistics(appStatData: List<List<Any>>, fromDate: LocalDate, toDate: LocalDate): List<ApplicationStatData> {
        var count: Int
        var platform: String
        var statDate: String
        var date: LocalDate
        var applicationStatDataList = mutableListOf<ApplicationStatData>()
        var i = 0

        date = fromDate
        while (date <= toDate) {
            applicationStatDataList.add(ApplicationStatData(0, Platform.IOS.name, date.format(DateTimeFormatter.ISO_DATE)))
            applicationStatDataList.add(ApplicationStatData(0, Platform.ANDROID.name, date.format(DateTimeFormatter.ISO_DATE)))
            date = date.plusDays(1)
        }

        while (i < appStatData.count()) {
             val item: Any = appStatData[i]
            if (item is Array<*>) {
                date = (item[2] as Date).toLocalDate()
                if (date in fromDate..toDate) {
                    count = (item[0] as BigInteger).toInt()
                    platform = item[1] as String
                    statDate = date.format(DateTimeFormatter.ISO_DATE)
                    val applicationStatData = ApplicationStatData(count, platform, statDate)
                    val data = applicationStatDataList.find {it.platform == platform && it.date == statDate}
                    if (data != null) {
                        val index = applicationStatDataList.indexOf(data)
                        applicationStatDataList[index] = applicationStatData
                    }
                }
            }
            i++
        }
        return applicationStatDataList
    }

    override fun restoreApp(appId: String) {
        if (appId.isBlank()) {
            return
        }

        val appData = applicationRepository.findFetchApplicationByAppId(appId)
        appData.deletedDate = null
        appData.status = ApplicationStatus.AVAILABLE
        applicationRepository.save(appData)
    }

    override fun markAppAsInvalid(appId: String) {
        if (appId.isBlank()) {
            return
        }

        val appData = applicationRepository.findFetchApplicationByAppId(appId)
        appData.valid = false
        applicationRepository.save(appData)
    }

}