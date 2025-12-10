package ru.mint.mobile.store.parser.rest.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import ru.mint.mobile.store.parser.repository.entity.SettingName
import ru.mint.mobile.store.parser.service.AppSettingsService

@Controller
@RequestMapping("/prefs")
class AppSettingsEditorController {

    @Autowired
    private lateinit var appSettingsService: AppSettingsService

    @GetMapping("/updater/get")
    fun updaterGet(): ResponseEntity<Long> {
        return ResponseEntity(appSettingsService.getParsingDataUpdateInterval(), HttpStatus.OK)
    }

    @PostMapping("/updater/set")
    fun updaterSet(@RequestBody interval: Long): ResponseEntity<Boolean> {
        return ResponseEntity(appSettingsService.setSettingValue(SettingName.PARSING_DATA_UPDATE_INTERVAL, interval.toString()), HttpStatus.OK)
    }

    @GetMapping("/topparser/get")
    fun topParserGet(): ResponseEntity<Long> {
        return ResponseEntity(appSettingsService.getParseTopInterval(), HttpStatus.OK)
    }

    @PostMapping("/topparser/set")
    fun topParserSet(@RequestBody interval: Long): ResponseEntity<Boolean> {
        return ResponseEntity(appSettingsService.setSettingValue(SettingName.PARSE_TOP_INTERVAL, interval.toString()), HttpStatus.OK)
    }

    @GetMapping("/pinger/get")
    fun pingerGet(): ResponseEntity<Long> {
        return ResponseEntity(appSettingsService.getAppPingerInterval(), HttpStatus.OK)
    }

    @PostMapping("/pinger/set")
    fun pingerSet(@RequestBody interval: Long): ResponseEntity<Boolean> {
        return ResponseEntity(appSettingsService.setSettingValue(SettingName.APPS_PINGER_INTERVAL, interval.toString()), HttpStatus.OK)
    }
}