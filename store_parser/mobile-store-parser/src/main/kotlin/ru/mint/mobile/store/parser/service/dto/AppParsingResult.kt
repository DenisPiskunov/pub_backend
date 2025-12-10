package ru.mint.mobile.store.parser.service.dto

data class AppParsingResult(
        val appUrl: String,
        val title: String,
        val iconUrl: String,
        val descrToLangs: MutableMap<String, MutableList<String>>,
        val fullData: String)