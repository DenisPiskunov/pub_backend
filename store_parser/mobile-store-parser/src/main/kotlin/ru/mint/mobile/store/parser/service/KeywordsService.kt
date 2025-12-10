package ru.mint.mobile.store.parser.service

import ru.mint.mobile.store.parser.rest.domain.KeywordsBundle

interface KeywordsService {
    fun getKeywords(type: String): List<String>
    fun editKeywords(keywordsBundle: KeywordsBundle): Boolean
}