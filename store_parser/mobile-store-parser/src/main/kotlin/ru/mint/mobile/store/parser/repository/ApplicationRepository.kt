package ru.mint.mobile.store.parser.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import ru.mint.mobile.store.parser.repository.entity.Application
import ru.mint.mobile.store.parser.repository.entity.ApplicationStatus
import ru.mint.mobile.store.parser.repository.entity.Platform


@Transactional(readOnly = true)
interface ApplicationRepository : JpaRepository<Application, Long> {
    @EntityGraph(attributePaths = ["parsingDataList"])
    fun findDistinctFetchAppParsingDataByStatusInAndValid(statuses: List<ApplicationStatus>, valid: Boolean): List<Application>
    fun findDistinctByPlatformAndStatusInAndValidOrderByCreationDateDesc(platform: Platform, statuses: List<ApplicationStatus>, valid: Boolean): List<Application>
    fun existsByAppId(appId: String): Boolean
    @EntityGraph(attributePaths = ["parsingDataList"])
    fun findFetchApplicationByAppId(appId: String): Application

    @Query(value = "SELECT count(a.app_id) , " +
            " a.platform, " +
            " cast(a.deleted_date AS DATE) deleted_date " +
            " FROM application a " +
            " WHERE a.status = 'UNAVAILABLE' " +
            " AND a.deleted_date IS NOT NULL " +
            " AND a.valid = TRUE " +
            " GROUP BY a.platform, cast(a.deleted_date AS DATE) " +
            " ORDER BY deleted_date ", nativeQuery = true)
    fun findFetchApplicationByDeletedDateInterval(): List<List<Any>>

    @Query(value = "SELECT count(a.app_id), " +
        " a.platform, " +
        " cast(a.creation_date AS DATE) creation_date " +
        " FROM application a " +
        " WHERE  a.valid = TRUE " +
        " GROUP BY a.platform, cast(a.creation_date AS DATE) " +
        " ORDER BY creation_date ", nativeQuery = true)
    fun findFetchAddedApplications(): List<List<Any>>

    @Query(value = "select * from get_active_apps()", nativeQuery = true)
    fun findFetchActiveApplications(): List<List<Any>>

}
