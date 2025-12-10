package ru.mint.mobile.store.parser.repository.entity

import javax.persistence.*

@Table(name = "app_market_data")
@Entity
data class AppMarketData (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(name = "package") val appPackage: String,
    @Column(name = "json") val jsonData: String,
    var removed: Boolean) {
        constructor(appPackage: String, jsonData: String, removed: Boolean) : this(null, appPackage, jsonData, removed)
}
