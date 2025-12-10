package ru.mint.mobile.store.parser.service.dto

data class ShortAndroidAppMarketData (
    var short_desc: String? = null,
    var title : String? = null,
    var package_name : String? = null,
    var category : String? = null,
    var icon_72 : String? = null,
    var market_url : String? = null
    )