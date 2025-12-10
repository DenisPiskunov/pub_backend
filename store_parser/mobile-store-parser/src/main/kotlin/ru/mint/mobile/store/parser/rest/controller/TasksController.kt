package ru.mint.mobile.store.parser.rest.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import ru.mint.mobile.store.parser.repository.entity.SettingName
import ru.mint.mobile.store.parser.service.ScheduledTasks

@Controller
@RequestMapping("/tasks", consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
class TasksController {

    @Autowired
    private lateinit var scheduledTasks: ScheduledTasks

    @PostMapping("/pinger/restart")
    fun pingerRestart(@RequestBody hard: Boolean): ResponseEntity<Boolean> {
        scheduledTasks.recreateTasks(SettingName.APPS_PINGER_INTERVAL, hard)
        return ResponseEntity(true, HttpStatus.OK)
    }

    @PostMapping("/topparser/restart")
    fun topParserRestart(@RequestBody hard: Boolean): ResponseEntity<Boolean> {
        scheduledTasks.recreateTasks(SettingName.PARSE_TOP_INTERVAL, hard)
        return ResponseEntity(true, HttpStatus.OK)
    }

    @PostMapping("/updater/restart")
    fun updaterRestart(@RequestBody hard: Boolean): ResponseEntity<Boolean> {
        scheduledTasks.recreateTasks(SettingName.PARSING_DATA_UPDATE_INTERVAL, hard)
        return ResponseEntity(true, HttpStatus.OK)
    }
}