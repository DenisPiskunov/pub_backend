package ru.mint.mobile.store.parser.service.dto

import ru.mint.mobile.store.parser.repository.entity.Application
import ru.mint.mobile.store.parser.repository.entity.ApplicationStatus
import ru.mint.mobile.store.parser.repository.entity.Platform
import java.time.format.DateTimeFormatter


data class ApplicationDetails(val id: Long? = null,
                              val appId: String,
                              val platform: Platform,
                              val status: ApplicationStatus,
                              val creationDate: String,
                              val title: String?,
                              val url: String?,
                              val iconUrl: String?,
                              val deletedDate: String?,
                              val keywords: String?,
                              val category: String?,
                              val categoryKey: String?) {

    companion object Factory {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        fun create(app: Application): ApplicationDetails {
            val deletedDate = if (app.deletedDate != null) {
                app.deletedDate!!.format(dateFormatter)
            } else {
                null
            }
            return ApplicationDetails(app.id, app.appId, app.platform, app.status, app.creationDate.format(dateFormatter), app.title, app.url, app.iconUrl, deletedDate, app.keyword, app.category, app.categoryKey)
        }


    }
}