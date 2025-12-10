package ru.mint.mobile.store.parser.repository.entity

import javax.persistence.*

@Table(name = "apps_categories")
@Entity
data class AppsCategories(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
        @Column(name = "platform") val platform: String,
        @Column(name = "category_key") val key: String,
        @Column(name = "category_name") val name: String,
        @Column(name = "search") val search: Boolean) {
    constructor(platform: String, key: String, name: String, search: Boolean): this(null, platform, key, name, search)
}