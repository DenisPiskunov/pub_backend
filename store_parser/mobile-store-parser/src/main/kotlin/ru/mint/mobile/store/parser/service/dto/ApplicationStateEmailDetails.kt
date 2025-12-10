package ru.mint.mobile.store.parser.service.dto

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class ApplicationStateEmailDetails(val appTitle: String,
                                        val appUrl: String,
                                        val iconUrl: String,
                                        val state: ApplicationState?,
                                        val descrToLangs: Map<String, List<String>>,
                                        val keyword: String?,
                                        val creationDate: String?) {
    companion object Factory {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        fun create(title: String, url: String, iconUrl: String, state: ApplicationState?, lastDescrToLangs: Map<String, List<String>>, keyword: String?, creationDate: LocalDateTime) =
                ApplicationStateEmailDetails(title, url, iconUrl, state, lastDescrToLangs, keyword, creationDate.format(dateFormatter))
    }
}