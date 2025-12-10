package ru.mint.mobile.store.parser.service.impl

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.mint.mobile.store.parser.repository.AppsCategoriesRepository
import ru.mint.mobile.store.parser.repository.entity.AppsCategories
import ru.mint.mobile.store.parser.service.AppSettingsService
import ru.mint.mobile.store.parser.service.CategoriesService
import ru.mint.mobile.store.parser.service.TopProductParserService
import java.net.URL

@Service
class CategoriesServiceImpl : CategoriesService {
    @Autowired
    private lateinit var appsCategoriesRepository: AppsCategoriesRepository
    @Autowired
    private lateinit var appSettingsService: AppSettingsService
    
    private companion object {
        val logger: Logger = LoggerFactory.getLogger(TopProductParserService::class.java)
    }

    override fun getAllCategories(): List<AppsCategories> {
        return appsCategoriesRepository.findFetchAppCategories()
    }

    override fun updateCategories(categories: List<AppsCategories>) {
       appsCategoriesRepository.save(categories)
    }

    override fun downloadAllCategories() {
        val parser = Parser()
        var allCategoriesList = mutableListOf<AppsCategories>()
        var iosStoreCategoriesResponse = StringBuilder("{\"genres\":[]}")
        var androidStoreCategoriesResponse = StringBuilder("{\"categories\":[]}")
        try {
            androidStoreCategoriesResponse = StringBuilder(URL(appSettingsService.getStoreCategoriesAndroid()).readText())
        }
        catch (e: Exception) {
            logger.debug(e.message)
        }
        finally {
            val androidCategoriesArray = ((parser.parse(androidStoreCategoriesResponse) as JsonObject).map)["categories"] as JsonArray<JsonObject>
            if (androidCategoriesArray.isNotEmpty()) {
                androidCategoriesArray.forEach {
                    val catKey = it["cat_key"].toString()
                    val catName = it["name"] as String
                    if (!appsCategoriesRepository.existsByKey(catKey)) {
                        var androidCat = AppsCategories("android", catKey, catName, false)
                        allCategoriesList.add(androidCat)
                    } else {

                    }
                }
            }
        }

        try {
            iosStoreCategoriesResponse = StringBuilder(URL(appSettingsService.getStoreCategoriesIOS()).readText())
        }
        catch (e: Exception) {
            logger.debug(e.message)
        }
        finally {
            val iosCategoriesArray = ((parser.parse(iosStoreCategoriesResponse) as JsonObject).map)["genres"] as JsonArray<JsonObject>
            if (iosCategoriesArray.isNotEmpty()) {
                iosCategoriesArray.forEach {
                    val catKey = it["genreId"].toString()
                    val catName = it["name"] as String
                    if (!appsCategoriesRepository.existsByKey(catKey)) {
                        var androidCat = AppsCategories("ios", catKey, catName, false)
                        allCategoriesList.add(androidCat)
                    }
                }
            }
        }
        appsCategoriesRepository.save(allCategoriesList)
    }

}