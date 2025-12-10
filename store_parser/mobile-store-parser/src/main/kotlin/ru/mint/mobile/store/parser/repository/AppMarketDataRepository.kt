package ru.mint.mobile.store.parser.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.mint.mobile.store.parser.repository.entity.AppMarketData

interface AppMarketDataRepository : JpaRepository<AppMarketData, Long> {
    fun findOneByAppPackage(appPackage: String): AppMarketData
    fun existsByAppPackage(appPackage: String): Boolean
    fun findDistinctFetchByRemovedOrderByAppPackage(removed: Boolean): List<AppMarketData>
    @Query(value = "select * from app_market_data where removed = false order by package;", nativeQuery = true)
    fun findFetchActiveAppMarketData(): List<AppMarketData>


}