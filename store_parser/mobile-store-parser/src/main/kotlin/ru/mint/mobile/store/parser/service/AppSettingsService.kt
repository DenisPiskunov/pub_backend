package ru.mint.mobile.store.parser.service

import ru.mint.mobile.store.parser.repository.entity.SettingName

interface AppSettingsService {
    fun setSettingValue(settingName: SettingName, value: String): Boolean
    fun findAll()
    fun getITunesAppUrlTemplate(): String
    fun getAndroidAppUrlTemplate(): String
    fun getITunesTopUrl(): String
    fun getAndroidTopUrl(): String
    fun getStoreCategoriesAndroid(): String
    fun getStoreCategoriesIOS(): String
    fun getCheckCategoriesOnStart(): Boolean
    fun getAndroidAppUrlTemplateSimple(): String
    fun getITunesAppUrlTemplateSimple(): String
    fun getRemovedAppIconUrl(): String
    fun getLuminatiUsername(): String
    fun getLuminatiPassword(): String
    fun getRequestMaxFailures(): Int
    fun getLuminatiProxyPort(): Int
    fun getSenderEmail(): String
    fun getSenderPassword(): String
    fun getSmtpHost(): String
    fun getSmtpPort(): Int
    fun getSmtpStartTLS(): Boolean
    fun getSmtpAuth(): Boolean
    fun getParsingDataUpdateInterval(): Long
    fun getParseTopInterval(): Long
    fun getAppPingerInterval(): Long
    fun getTopParserAndroidMainContainerKey(): String
    fun getTopParserAndroidDescriptionKey(): String
    fun getTopParserAndroidReviewsKey(): String
    fun getTopParserAndroidScreenshotsKey(): String
    fun getTopParserAndroidTitleKey(): String
    fun getTopParserAndroidIconUrlKey(): String
    fun getTopParserAndroidCardsKey(): String
    fun detTopParserAndroidAppIDInCard(): String
    fun getUseLuminati(): Boolean
    fun getAdminEmail(): String
    fun getWordsSearchRegex(): String
}