package ru.mint.mobile.store.parser.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.mint.mobile.store.parser.repository.AppSettingsRepository
import ru.mint.mobile.store.parser.repository.entity.SettingName
import ru.mint.mobile.store.parser.service.AppSettingsService
import ru.mint.mobile.store.parser.service.ScheduledTasks

@Service
class AppSettingsServiceImpl : AppSettingsService {

    private var senderEmail = ""
    private var senderPassword = ""
    private var smtpAuthStr = ""
    private var smtpStartTLSStr = ""
    private var smtpHost = ""
    private var smtpPort = 0
    private var luminatiUsername = ""
    private var luminatiPassword = ""
    private var requestMaxFailures = 0
    private var luminatiProxyPort = 0
    private var removedAppIconUrl = ""
    private var iTunesAppUrlTemplate = ""
    private var androidAppUrlTemplate = ""
    private var iTunesAppUrlTemplateSimple = ""
    private var androidAppUrlTemplateSimple = ""
    private var iTunesTopUrlTemplate = ""
    private var androidTopUrlTemplate = ""
    private var topParserAndroidMainContainerKey = ""
    private var topParserAndroidDescriptionKey = ""
    private var topParserAndroidReviewsKey = ""
    private var topParserAndroidScreenshotsKey = ""
    private var topParserAndroidTitleKey = ""
    private var topParserAndroidIconUrlKey = ""
    private var topParserAndroidCardsKey = ""
    private var topParserAndroidAppIDInCard = ""
    private var useLuminatiForGetTop = ""
    private var adminEmail = ""
    private var wordsSearchRegex = ""
    private var storeCategoriesAndroid = ""
    private var storeCategoriesIOS = ""
    private var checkCategoriesOnStart = ""



    @Autowired
    private lateinit var appSettingsRepository: AppSettingsRepository
    @Autowired
    private lateinit var scheduledTasks: ScheduledTasks

    override fun findAll() {
        val settingsMap = appSettingsRepository.findAll().associateBy({ it.settingName.name }, { it.settingValue })
        senderEmail = settingsMap.getValue(SettingName.SENDER_EMAIL.name)
        senderPassword = settingsMap.getValue(SettingName.SENDER_PASSWORD.name)
        smtpAuthStr = settingsMap.getValue(SettingName.MAIL_SMTP_AUTH.name)
        smtpStartTLSStr = settingsMap.getValue(SettingName.MAIL_SMTP_STARTTLS_ENABLE.name)
        smtpHost = settingsMap.getValue(SettingName.MAIL_SMTP_HOST.name)
        smtpPort = settingsMap.getValue(SettingName.MAIL_SMTP_PORT.name).toInt()
        luminatiUsername = settingsMap.getValue(SettingName.LUMINATI_USERNAME.name)
        luminatiPassword = settingsMap.getValue(SettingName.LUMINATI_PSWD.name)
        requestMaxFailures = settingsMap.getValue(SettingName.LUMINATI_REQUEST_MAX_FAILURES.name).toInt()
        luminatiProxyPort = settingsMap.getValue(SettingName.LUMINATI_PROXY_PORT.name).toInt()
        removedAppIconUrl = settingsMap.getValue(SettingName.REMOVED_APP_ICON_URL.name)
        iTunesAppUrlTemplate = settingsMap.getValue(SettingName.ITUNES_APP_URL_TEMPLATE.name)
        androidAppUrlTemplate = settingsMap.getValue(SettingName.ANDROID_APP_URL_TEMPLATE.name)
        iTunesAppUrlTemplateSimple = settingsMap.getValue(SettingName.ITUNES_APP_URL_TEMPLATE_SIMPLE.name)
        androidAppUrlTemplateSimple = settingsMap.getValue(SettingName.ANDROID_APP_URL_TEMPLATE_SIMPLE.name)
        iTunesTopUrlTemplate = settingsMap.getValue(SettingName.IOS_TOP_APPS_URL_TEMPLATE.name)
        androidTopUrlTemplate = settingsMap.getValue(SettingName.ANDROID_TOP_APPS_URL_TEMPLATE.name)
//        storeCategoriesAndroid = settingsMap.getValue(SettingName.STORE_CATEGORIES_ANDROIDD.name)
//        storeCategoriesIOS = settingsMap.getValue(SettingName.STORE_CATEGORIES_IOS.name)
//        checkCategoriesOnStart = settingsMap.getValue(SettingName.CHECK_CATEGORIES_ON_START.name)
        topParserAndroidMainContainerKey = settingsMap.getValue(SettingName.TOP_PARSER_ANDROID_MAIN_CONTAINER_KEY.name)
        topParserAndroidDescriptionKey = settingsMap.getValue(SettingName.TOP_PARSER_ANDROID_DESCRIPTION_KEY.name)
        topParserAndroidReviewsKey = settingsMap.getValue(SettingName.TOP_PARSER_ANDROID_REVIEWS_KEY.name)
        topParserAndroidScreenshotsKey = settingsMap.getValue(SettingName.TOP_PARSER_ANDROID_SCREENSHOTS_KEY.name)
        topParserAndroidTitleKey = settingsMap.getValue(SettingName.TOP_PARSER_ANDROID_TITLE_KEY.name)
        topParserAndroidIconUrlKey = settingsMap.getValue(SettingName.TOP_PARSER_ANDROID_ICON_URL_KEY.name)
        topParserAndroidCardsKey = settingsMap.getValue(SettingName.TOP_PARSER_ANDROID_CARDS_KEY.name)
        topParserAndroidAppIDInCard = settingsMap.getValue(SettingName.TOP_PARSER_ANDROID_APPID_IN_CARD_KEY.name)
        useLuminatiForGetTop = settingsMap.getValue(SettingName.USE_LUMINATI_FOR_GET_TOP.name)
        adminEmail = settingsMap.getValue(SettingName.ADMIN_EMAIL.name)
        wordsSearchRegex = settingsMap.getValue(SettingName.WORDS_SEARCH_REGEX.name)
    }

    override fun setSettingValue(settingName: SettingName, value: String): Boolean {
        return try {
            val settingItem = appSettingsRepository.findBySettingName(settingName)
            settingItem.settingValue = value
            appSettingsRepository.save(settingItem)
            if (settingName == SettingName.APPS_PINGER_INTERVAL ||
                    settingName == SettingName.PARSE_TOP_INTERVAL ||
                    settingName == SettingName.PARSING_DATA_UPDATE_INTERVAL) {
                scheduledTasks.recreateTasks(settingName, false)
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    override fun getITunesAppUrlTemplate(): String {
        if (iTunesAppUrlTemplate.isBlank()) {
            iTunesAppUrlTemplate = appSettingsRepository.findBySettingName(SettingName.ITUNES_APP_URL_TEMPLATE).settingValue
        }
        return iTunesAppUrlTemplate
    }

    override fun getAndroidAppUrlTemplate(): String {
        if (androidAppUrlTemplate.isBlank()) {
            androidAppUrlTemplate = appSettingsRepository.findBySettingName(SettingName.ANDROID_APP_URL_TEMPLATE).settingValue
        }
        return androidAppUrlTemplate
    }

    override fun getITunesTopUrl(): String {
        if (iTunesTopUrlTemplate.isBlank()) {
            iTunesTopUrlTemplate = appSettingsRepository.findBySettingName(SettingName.IOS_TOP_APPS_URL_TEMPLATE).settingValue
        }
        return iTunesTopUrlTemplate
    }

    override fun getAndroidTopUrl(): String {
        if (androidTopUrlTemplate.isBlank()) {
            androidTopUrlTemplate = appSettingsRepository.findBySettingName(SettingName.ANDROID_TOP_APPS_URL_TEMPLATE).settingValue
        }
        return androidTopUrlTemplate
    }

    override fun getStoreCategoriesAndroid(): String {
        if (storeCategoriesAndroid.isBlank()) {
            storeCategoriesAndroid = appSettingsRepository.findBySettingName(SettingName.STORE_CATEGORIES_ANDROID).settingValue
        }
        return storeCategoriesAndroid
    }


    override fun getStoreCategoriesIOS(): String {
        if (storeCategoriesIOS.isBlank()) {
            storeCategoriesIOS = appSettingsRepository.findBySettingName(SettingName.STORE_CATEGORIES_IOS).settingValue
        }
        return storeCategoriesIOS
    }

    override fun getCheckCategoriesOnStart(): Boolean {
        if (checkCategoriesOnStart.isBlank()) {
            checkCategoriesOnStart = appSettingsRepository.findBySettingName(SettingName.CHECK_CATEGORIES_ON_START).settingValue
        }
        return checkCategoriesOnStart.toBoolean()
    }

    override fun getAndroidAppUrlTemplateSimple(): String {
        if (androidAppUrlTemplateSimple.isBlank()) {
            androidAppUrlTemplateSimple = appSettingsRepository.findBySettingName(SettingName.ANDROID_APP_URL_TEMPLATE_SIMPLE).settingValue
        }
        return androidAppUrlTemplateSimple
    }

    override fun getITunesAppUrlTemplateSimple(): String {
        if (iTunesAppUrlTemplateSimple.isBlank()) {
            iTunesAppUrlTemplateSimple = appSettingsRepository.findBySettingName(SettingName.ITUNES_APP_URL_TEMPLATE_SIMPLE).settingValue
        }
        return iTunesAppUrlTemplateSimple
    }

    override fun getRemovedAppIconUrl(): String {
        if (removedAppIconUrl.isBlank()) {
            removedAppIconUrl = appSettingsRepository.findBySettingName(SettingName.REMOVED_APP_ICON_URL).settingValue
        }
        return removedAppIconUrl
    }

    override fun getLuminatiUsername(): String {
        if (luminatiUsername.isBlank() ) {
            luminatiUsername = appSettingsRepository.findBySettingName(SettingName.LUMINATI_USERNAME).settingValue
        }
        return luminatiUsername
    }

    override fun getLuminatiPassword(): String {
        if (luminatiPassword.isBlank()) {
            luminatiPassword = appSettingsRepository.findBySettingName(SettingName.LUMINATI_PSWD).settingValue
        }
        return luminatiPassword
    }

    override fun getRequestMaxFailures(): Int {
        if (requestMaxFailures == 0) {
            requestMaxFailures = appSettingsRepository.findBySettingName(SettingName.LUMINATI_REQUEST_MAX_FAILURES).settingValue.toInt()
        }
        return requestMaxFailures
    }

    override fun getLuminatiProxyPort(): Int {
        if (luminatiProxyPort == 0) {
            luminatiProxyPort = appSettingsRepository.findBySettingName(SettingName.LUMINATI_PROXY_PORT).settingValue.toInt()
        }
        return luminatiProxyPort
    }

    override fun getSenderEmail(): String {
        if (senderEmail.isBlank()) {
            senderEmail = appSettingsRepository.findBySettingName(SettingName.SENDER_EMAIL).settingValue
        }
        return senderEmail
    }

    override fun getSenderPassword(): String {
        if (senderPassword.isBlank()) {
            senderPassword = appSettingsRepository.findBySettingName(SettingName.SENDER_PASSWORD).settingValue
        }
        return senderPassword
    }

    override fun getSmtpHost(): String {
        if (smtpHost.isBlank()) {
            smtpHost = appSettingsRepository.findBySettingName(SettingName.MAIL_SMTP_HOST).settingValue
        }
        return smtpHost
    }

    override fun getSmtpPort(): Int {
        if (smtpPort == 0) {
            smtpPort = appSettingsRepository.findBySettingName(SettingName.MAIL_SMTP_PORT).settingValue.toInt()
        }
        return smtpPort
    }

    override fun getSmtpStartTLS(): Boolean {
        if (smtpStartTLSStr.isBlank()) {
            smtpStartTLSStr = appSettingsRepository.findBySettingName(SettingName.MAIL_SMTP_STARTTLS_ENABLE).settingValue
        }
        return smtpStartTLSStr.toBoolean()
    }

    override fun getSmtpAuth(): Boolean {
        if (smtpAuthStr.isBlank()) {
            smtpAuthStr = appSettingsRepository.findBySettingName(SettingName.MAIL_SMTP_AUTH).settingValue
        }
        return smtpAuthStr.toBoolean()
    }

    override fun getParsingDataUpdateInterval(): Long {
        return appSettingsRepository.findBySettingName(SettingName.PARSING_DATA_UPDATE_INTERVAL).settingValue.toLong()
    }

    override fun getParseTopInterval(): Long {
        return appSettingsRepository.findBySettingName(SettingName.PARSE_TOP_INTERVAL).settingValue.toLong()
    }

    override fun getAppPingerInterval(): Long {
        return appSettingsRepository.findBySettingName(SettingName.APPS_PINGER_INTERVAL).settingValue.toLong()

    }

    override fun getTopParserAndroidMainContainerKey(): String {
        if (topParserAndroidMainContainerKey.isBlank()) {
            topParserAndroidMainContainerKey = appSettingsRepository.findBySettingName(SettingName.TOP_PARSER_ANDROID_MAIN_CONTAINER_KEY).settingValue
        }
        return topParserAndroidMainContainerKey
    }


    override fun getTopParserAndroidDescriptionKey(): String {
        if (topParserAndroidDescriptionKey.isBlank()) {
            topParserAndroidDescriptionKey = appSettingsRepository.findBySettingName(SettingName.TOP_PARSER_ANDROID_DESCRIPTION_KEY).settingValue
        }
        return topParserAndroidDescriptionKey
    }

    override fun getTopParserAndroidReviewsKey(): String {
        if (topParserAndroidReviewsKey.isBlank()) {
            topParserAndroidReviewsKey = appSettingsRepository.findBySettingName(SettingName.TOP_PARSER_ANDROID_REVIEWS_KEY).settingValue
        }
        return topParserAndroidReviewsKey
    }

    override fun getTopParserAndroidScreenshotsKey(): String {
        if (topParserAndroidScreenshotsKey.isBlank()) {
            topParserAndroidScreenshotsKey = appSettingsRepository.findBySettingName(SettingName.TOP_PARSER_ANDROID_SCREENSHOTS_KEY).settingValue
        }
        return topParserAndroidScreenshotsKey
    }

    override fun getTopParserAndroidTitleKey(): String {
        if (topParserAndroidTitleKey.isBlank()) {
            topParserAndroidTitleKey = appSettingsRepository.findBySettingName(SettingName.TOP_PARSER_ANDROID_TITLE_KEY).settingValue
        }
        return topParserAndroidTitleKey
    }

    override fun getTopParserAndroidIconUrlKey(): String {
        if (topParserAndroidIconUrlKey.isBlank()) {
            topParserAndroidIconUrlKey = appSettingsRepository.findBySettingName(SettingName.TOP_PARSER_ANDROID_ICON_URL_KEY).settingValue
        }
        return topParserAndroidIconUrlKey
    }


    override fun getTopParserAndroidCardsKey(): String {
        if (topParserAndroidCardsKey.isBlank()) {
            topParserAndroidCardsKey = appSettingsRepository.findBySettingName(SettingName.TOP_PARSER_ANDROID_CARDS_KEY).settingValue
        }
        return topParserAndroidCardsKey
    }

    override fun detTopParserAndroidAppIDInCard(): String {
        if (topParserAndroidAppIDInCard.isBlank()) {
            topParserAndroidAppIDInCard = appSettingsRepository.findBySettingName(SettingName.TOP_PARSER_ANDROID_APPID_IN_CARD_KEY).settingValue
        }
        return topParserAndroidAppIDInCard
    }

    override fun getUseLuminati(): Boolean {
        if (useLuminatiForGetTop.isBlank()) {
            useLuminatiForGetTop = appSettingsRepository.findBySettingName(SettingName.USE_LUMINATI_FOR_GET_TOP).settingValue
        }
        return useLuminatiForGetTop.toBoolean()
    }

    override fun getWordsSearchRegex(): String {
        if (wordsSearchRegex.isBlank()) {
            wordsSearchRegex = appSettingsRepository.findBySettingName(SettingName.WORDS_SEARCH_REGEX).settingValue
        }
        return wordsSearchRegex
    }

    override fun getAdminEmail(): String {
        if (adminEmail.isBlank()) {
            adminEmail = appSettingsRepository.findBySettingName(SettingName.WORDS_SEARCH_REGEX).settingValue
        }
        return adminEmail
    }
}