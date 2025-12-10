package ru.mint.mobile.store.parser.repository.entity

import javax.persistence.*

@Table(name = "app_settings")
@Entity
data class AppSettings(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
        @Enumerated(EnumType.STRING) @Column(name = "setting_name") val settingName: SettingName,
        @Column(name = "setting_value") var settingValue: String)