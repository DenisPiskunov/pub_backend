package ru.mint.mobile.store.parser.rest.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import ru.mint.mobile.store.parser.rest.domain.NewAppData
import ru.mint.mobile.store.parser.service.ApplicationService
import ru.mint.mobile.store.parser.service.dto.AppAdditionalData
import ru.mint.mobile.store.parser.service.dto.ApplicationAllStatData
import ru.mint.mobile.store.parser.service.dto.ApplicationDetails
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Controller
@RequestMapping("/app", consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
class ApplicationCommandController {

    @Autowired
    private lateinit var applicationService: ApplicationService

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(ApplicationCommandController::class.java)
    }

    @PostMapping("/create")
    fun create(@RequestBody appData: List<NewAppData>): ResponseEntity<Boolean> {
        applicationService.createAppWithFilter(appData)
        return ResponseEntity(true, HttpStatus.OK)
    }

    @PostMapping("/restore")
    fun restore(@RequestBody appId: String): ResponseEntity<Boolean> {
        applicationService.restoreApp(appId)
        return ResponseEntity(true, HttpStatus.OK)
    }

    @PostMapping("/mark_as_invalid")
    fun markInvalid(@RequestBody appId: String): ResponseEntity<Boolean> {
        applicationService.markAppAsInvalid(appId)
        return ResponseEntity(true, HttpStatus.OK)
    }

    @GetMapping("/forbidden")
    fun forbidden(): ResponseEntity<Void> = ResponseEntity.status(HttpStatus.FORBIDDEN).build()

    @GetMapping("/list")
    fun list(@RequestParam(value = "platform", defaultValue = "IOS") platform: String, @RequestParam(value = "status", defaultValue = "AVAILABLE") status: String): ResponseEntity<List<ApplicationDetails>> {
        return ResponseEntity(applicationService.getAllApps(platform, status), HttpStatus.OK)
    }

    @GetMapping("/getdata")
    fun getdata(@RequestParam(value = "appid", defaultValue = "") appId: String) : ResponseEntity<AppAdditionalData> {
        return ResponseEntity(applicationService.getSingleAppData(appId), HttpStatus.OK)
    }

    @GetMapping("/getstatistics/app_statistics")
    fun getAppStatistics(from: String, to: String):  ResponseEntity<List<ApplicationAllStatData>> {
        val fromDate= LocalDate.parse(from, DateTimeFormatter.ISO_DATE)
        val toDate = LocalDate.parse(to, DateTimeFormatter.ISO_DATE)
        return ResponseEntity(applicationService.getAppStatistics(fromDate, toDate), HttpStatus.OK)

    }


}