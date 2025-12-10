package ru.mint.mobile.store.parser.service.impl

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.mint.mobile.store.parser.repository.AppMarketDataRepository
import ru.mint.mobile.store.parser.service.AppMarketDataService
import ru.mint.mobile.store.parser.service.dto.AppAvailability
import ru.mint.mobile.store.parser.service.dto.FullAndroidAppMarketData
import ru.mint.mobile.store.parser.service.dto.ShortAndroidAppMarketData

@Service
class AppMarketDataServiceImpl: AppMarketDataService {
    @Autowired
    private lateinit var appMarketDataRepository: AppMarketDataRepository

    override fun getAllShortAndroidMarketData(): List<ShortAndroidAppMarketData> {
        var shortAndroidMarketDataList = mutableListOf<ShortAndroidAppMarketData>()
        val appMarketDataList = appMarketDataRepository.findDistinctFetchByRemovedOrderByAppPackage(false)
        appMarketDataList.forEach{ appData ->
            val map = (Parser().parse(StringBuilder(appData.jsonData)) as JsonObject).map
            var shortDataItem = ShortAndroidAppMarketData(
                    title = map["title"]?.toString(),
                    package_name = map["package_name"]?.toString(),
                    short_desc = map["short_desc"]?.toString(),
                    category = map["category"]?.toString(),
                    icon_72 = map["icon_72"]?.toString(),
                    market_url = map["market_url"]?.toString())
            shortAndroidMarketDataList.add(shortDataItem)
        }
        return shortAndroidMarketDataList
    }

    override fun getSingleFullAndroidMarketData(appPackage: String): FullAndroidAppMarketData {
        var result = FullAndroidAppMarketData()
        if (appMarketDataRepository.existsByAppPackage(appPackage)) {
            val appMarketData = appMarketDataRepository.findOneByAppPackage(appPackage)
            val map = (Parser().parse(StringBuilder(appMarketData.jsonData)) as JsonObject).map
            if(map.isNotEmpty()) {
                result.physical_address = map["physical_address"]?.toString()
                result.short_desc = map["short_desc"]?.toString()
                if (map["app_availability"] != null) {
                    val availabilityObject = map["app_availability"] as JsonObject
                    val appAvailability = AppAvailability(
                            available_in = availabilityObject["available_in"] as List<String>?,
                            not_available_in = availabilityObject["not_available_in"] as List<String>? ,
                            availability_unknown = availabilityObject["availability_unknown"] as List<String>?,
                            package_name = availabilityObject["package_name"] as String?
                            )
                    result.app_availability = appAvailability
                }
                result.i18n_lang = map["i18n_lang"] as List<String>?
                result.price_i18n_countries = map["price_i18n_countries"] as List<String>?
                result.rating = map["rating"].toString() //as Double?
                result.downloads_min = map["downloads_min"].toString() // as Long?
                result.description  = map["description"]?.toString()
                result.what_is_new  = map["what_is_new"]?.toString()
                result.title  = map["title"]?.toString()
                result.screenshots = map["screenshots"] as List<String>?
                result.cat_keys = map["cat_keys"] as List<String>?
                result.market_source  = map["market_source"]?.toString()
                result.downloads  = map["downloads"]?.toString()
                if (map["permissions"] != null) {
                    val permisionsList = map["permissions"] as List<JsonObject>
                    if (permisionsList.isNotEmpty()) {
                        permisionsList.forEach { item ->
                            result.permissions?.add(item["id"].toString())
                        }
                    }
                }
                result.price  = map["price"]?.toString()
                result.price_currency  = map["price_currency"]?.toString()
                result.cat_int = map["cat_int"].toString() // as Int?
                result.size = map["size"]?.toString()// as Int?
                result.iap = map["iap"] as Boolean
                result.iap_min = map["iap_min"]?.toString() //as Double?
                result.iap_max = map["iap_max"]?.toString() //as Double?
                result.content_rating  = map["content_rating"]?.toString()
                result.lang  = map["lang"]?.toString()
                result.email  = map["email"]?.toString()
                result.ratings_2 = map["ratings_2"]?.toString()// as Int?
                result.similar = map["similar"] as List<String>?
                result.badges = map["badges"] as List<String>?
                result.interactive_elements = map["interactive_elements"] as List<String>?
                result.ratings_1 = map["ratings_1"].toString() // as Int?
                result.price_numeric = map["price_numeric"]?.toString() //as Double?
                result.ratings_4 = map["ratings_4"]?.toString()// as Int?
                result.ratings_3 = map["ratings_3"]?.toString()// as Int?
                result.ratings_5 = map["ratings_5"]?.toString()// as Int?
                result.created  = map["created"]?.toString()
                result.downloads_max = map["downloads_max"]?.toString()// as Long?
                result.cat_key  = map["cat_cat_key"]?.toString()
                result.version = map["version"]?.toString()
                result.market_update  = map["market_update"]?.toString()
                result.cat_type = map["cat_type"]?.toString()// as Int?
                result.package_name  = map["package_name"]?.toString()
                result.from_developer = map["from_developer"] as List<String>?
                result.category  = map["category"]?.toString()
                result.developer  = map["developer"]?.toString()
                result.number_ratings = map["number_ratings"]?.toString()// as Int?
                result.icon  = map["icon"]?.toString()
                result.icon_72  = map["icon_72"]?.toString()
                result.privacy_policy  = map["privacy_policy"]?.toString()
                result.featured_graphic  = map["featured_graphic"]?.toString()
                result.min_sdk  = map["min_sdk"]?.toString()
                result.website  = map["website"]?.toString()
                result.promo_video  = map["promo_video"]?.toString()
                result.market_status  = map["market_status"]?.toString()
                result.unpublished_timestamp  = map["unpublished_timestamp"]?.toString()
                result.market_url  = map["market_url"]?.toString()
                result.deep_link = map["deep_link"]?.toString()
            }
        }
        return result
    }

    override fun markAppMarketDataDeleted(appPackage: String) {
        changeStateAppMarketData(appPackage, true)
    }

    override fun restoreAppMarketData(appPackage: String) {
        changeStateAppMarketData(appPackage, false)
    }

    fun changeStateAppMarketData(appPackage: String, state: Boolean) {
        if (appPackage.isBlank()) {
            return
        }

        if (appMarketDataRepository.existsByAppPackage(appPackage)) {
            val appMarketData = appMarketDataRepository.findOneByAppPackage(appPackage)
            appMarketData.removed = state
            appMarketDataRepository.save(appMarketData)
        }
    }
}