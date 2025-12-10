package ru.mint.mobile.store.parser.service

import ru.mint.mobile.store.parser.rest.domain.NewAppData
import ru.mint.mobile.store.parser.service.dto.AppAdditionalData
import ru.mint.mobile.store.parser.service.dto.ApplicationAllStatData
import ru.mint.mobile.store.parser.service.dto.ApplicationDetails
import ru.mint.mobile.store.parser.service.dto.ApplicationStatData
import java.time.LocalDate

interface ApplicationService {
    fun createApp(appData: List<NewAppData>)
    fun createAppWithFilter(appData: List<NewAppData>)
    fun getAllApps(platform: String, status: String): List<ApplicationDetails>
    fun getSingleAppData(appId: String): AppAdditionalData
    fun getAppStatistics(fromDate: LocalDate, toDate: LocalDate): List<ApplicationAllStatData>
    fun restoreApp(appId: String)
    fun markAppAsInvalid(appId: String)
}