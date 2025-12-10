package ru.mint.mobile.store.parser.repository.entity

import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import ru.mint.mobile.store.parser.repository.PostgreSQLEnumType
import java.time.LocalDateTime
import javax.persistence.*


//TODO: move id to base class
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType::class)
@Entity
class Application(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
        @Column(name = "app_id") val appId: String,
        @Enumerated(EnumType.STRING) @Column(columnDefinition = "PLATFORM_TYPE") @Type(type = "pgsql_enum") val platform: Platform,
        @Enumerated(EnumType.STRING) @Column(columnDefinition = "APP_STATUS_TYPE") @Type(type = "pgsql_enum") var status: ApplicationStatus,
        @Column(name = "creation_date") val creationDate: LocalDateTime,
        var title: String? = null,
        var url: String? = null,
        @Column(name = "icon_url") var iconUrl: String? = null,
        @Column(name = "full_data") var fullData: String? = null,
        @Column(name = "deleted_date") var deletedDate:  LocalDateTime? = null,
        var keyword: String? = null,
        @Column(name = "migrated_data") var migratedData: String? = null,
        var category: String? = null,
        @Column(name = "category_key") var categoryKey: String? = null,
        var valid: Boolean = true,
        @OneToMany(mappedBy = "application", cascade = [(CascadeType.ALL)]) var parsingDataList: MutableList<ApplicationParsingData> = mutableListOf()) {

    constructor(appId: String, platform: Platform, status: ApplicationStatus, keyword: String) : this(null, appId, platform, status, LocalDateTime.now(), keyword = keyword)
    constructor(appId: String, platform: Platform, status: ApplicationStatus, title: String?, url: String?, iconUrl: String?, fullData: String?, keyword: String?) : this(null, appId, platform, status, LocalDateTime.now(), title, url, iconUrl, fullData, keyword = keyword)
    constructor(appId: String, platform: Platform, status: ApplicationStatus, title: String?, url: String?, iconUrl: String?, fullData: String?, keyword: String?, category: String?, categoryKey: String?, valid: Boolean) : this(null, appId, platform, status, LocalDateTime.now(), title, url, iconUrl, fullData, keyword = keyword, category = category, categoryKey = categoryKey, valid = valid)
}