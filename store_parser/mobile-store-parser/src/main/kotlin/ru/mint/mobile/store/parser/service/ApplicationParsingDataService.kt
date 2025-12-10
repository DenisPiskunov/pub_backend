package ru.mint.mobile.store.parser.service

import ru.mint.mobile.store.parser.repository.entity.Application


interface ApplicationParsingDataService {

    fun updateParsingData()
    fun searchInQueue()
    fun processApplication(application: Application, fastParse: Boolean = false): Application?
    fun processNewTopApplications()
    fun prepareAndSaveApplicationList(applications: List<Application>, fastParse: Boolean = false)
}