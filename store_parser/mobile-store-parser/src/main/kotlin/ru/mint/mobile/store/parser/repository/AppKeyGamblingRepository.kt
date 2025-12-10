package ru.mint.mobile.store.parser.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mint.mobile.store.parser.repository.entity.AppKeyGambling

interface AppKeyGamblingRepository : JpaRepository<AppKeyGambling, Long> {
    fun findOneByKey(key: String): AppKeyGambling
    fun existsByKey(key: String): Boolean
}