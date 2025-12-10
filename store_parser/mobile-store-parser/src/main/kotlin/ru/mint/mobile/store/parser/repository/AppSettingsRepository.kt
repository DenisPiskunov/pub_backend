package ru.mint.mobile.store.parser.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mint.mobile.store.parser.repository.entity.AppSettings
import ru.mint.mobile.store.parser.repository.entity.SettingName

interface AppSettingsRepository : JpaRepository<AppSettings, Long> {
    fun findBySettingName(settingName: SettingName) : AppSettings
}