package ru.mint.mobile.store.parser.service

import ru.mint.mobile.store.parser.service.dto.FullAndroidAppMarketData
import ru.mint.mobile.store.parser.service.dto.ShortAndroidAppMarketData

interface AppMarketDataService {
    fun getAllShortAndroidMarketData(): List<ShortAndroidAppMarketData>
    fun getSingleFullAndroidMarketData(appPackage: String): FullAndroidAppMarketData
    fun markAppMarketDataDeleted(appPackage: String)
    fun restoreAppMarketData(appPackage: String)
}
