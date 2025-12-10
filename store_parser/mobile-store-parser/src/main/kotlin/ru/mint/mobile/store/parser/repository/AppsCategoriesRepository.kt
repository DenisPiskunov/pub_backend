package ru.mint.mobile.store.parser.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.mint.mobile.store.parser.repository.entity.*

interface AppsCategoriesRepository : JpaRepository<AppsCategories, Long> {
    fun findByPlatform(platform: String) : List<AppsCategories>
    fun findByPlatformAndSearch(platform: String, search: Boolean): List<AppsCategories>
    fun existsByKey(catKey: String): Boolean
    @Query(value = "SELECT a.id, " +
            " a.platform, " +
            " a.category_key, " +
            " a.category_name, " +
            " a.search " +
            " FROM apps_categories a " +
            " ORDER BY a.platform, a.category_name ", nativeQuery = true)
    fun findFetchAppCategories(): List<AppsCategories>

}