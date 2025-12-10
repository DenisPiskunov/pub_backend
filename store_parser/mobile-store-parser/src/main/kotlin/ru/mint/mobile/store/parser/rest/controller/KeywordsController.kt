package ru.mint.mobile.store.parser.rest.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import ru.mint.mobile.store.parser.rest.domain.KeywordsBundle
import ru.mint.mobile.store.parser.service.KeywordsService

@Controller
@RequestMapping("/keywords", consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
class KeywordsController {
    @Autowired
    private lateinit var keywordsService: KeywordsService

    @PostMapping("/edit")
    fun edit(@RequestBody keywordsBundle: KeywordsBundle): ResponseEntity<Boolean> {
        return ResponseEntity(keywordsService.editKeywords(keywordsBundle), HttpStatus.OK)
    }

    @GetMapping("/get")
    fun get(@RequestParam(value = "wordstype") wordsType: String): ResponseEntity<List<String>> {
        return ResponseEntity(keywordsService.getKeywords(wordsType), HttpStatus.OK)
    }
}

