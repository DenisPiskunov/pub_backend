package ru.mint.mobile.store.parser.rest.controller


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import ru.mint.mobile.store.parser.service.AppMarketDataService
import ru.mint.mobile.store.parser.service.dto.FullAndroidAppMarketData
import ru.mint.mobile.store.parser.service.dto.ShortAndroidAppMarketData

@Controller
@RequestMapping("/app_market_data/android/")
class AppMarketDataController {
    @Autowired
    private lateinit var appMarketDataService: AppMarketDataService

    @GetMapping("/list")
    fun appMarketDataGetAll(): ResponseEntity<List<ShortAndroidAppMarketData>> {
        return ResponseEntity(appMarketDataService.getAllShortAndroidMarketData(), HttpStatus.OK)
    }

    @GetMapping("/fulldata")
    fun appMarketDataFullAppMarketData(@RequestParam(value = "app_package") appPackage: String): ResponseEntity<FullAndroidAppMarketData> {
        return ResponseEntity(appMarketDataService.getSingleFullAndroidMarketData(appPackage), HttpStatus.OK)
    }

    @PostMapping("/update")
    fun appMarketDataUpdate(@RequestBody appPackage: String): ResponseEntity<Void> {
        appMarketDataService.markAppMarketDataDeleted(appPackage)
        return ResponseEntity(HttpStatus.OK)
    }
}