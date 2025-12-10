package ru.mint.mobile.store.parser.service

import org.jsoup.nodes.Document
import ru.mint.mobile.store.parser.repository.entity.Application
import ru.mint.mobile.store.parser.repository.entity.Platform
import ru.mint.mobile.store.parser.service.dto.AppParsingResultWrapper


interface AppParserService {
    fun parse(appId: String, platform: Platform, fastParse: Boolean = false): AppParsingResultWrapper
    fun androidAppPageParse(doc: Document, appId: String): Application
    fun iosAppPageParse(doc: Document, appId: String): Application
}