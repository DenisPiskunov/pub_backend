package ru.mint.mobile.store.parser.service

import ru.mint.mobile.store.parser.repository.entity.Application

interface TopProductParserService {
    fun preinitParser()
    fun parseTopAndroidByCategories(categoryKey: String, category: String): MutableList<Application>
    fun parseTopIOSByCategories(categoryKey: String, category: String): MutableList<Application>
    //fun loadKeysList()
    //fun parseTop(): List<Application>
}