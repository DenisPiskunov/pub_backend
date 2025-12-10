package ru.mint.mobile.store.parser.rest.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import ru.mint.mobile.store.parser.repository.entity.AppsCategories

import ru.mint.mobile.store.parser.service.CategoriesService

@Controller
@RequestMapping("/categories", consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE)])
class AppsCategoriesEditorController {

    @Autowired
    private lateinit var appsCategoriesService: CategoriesService

    @GetMapping("/list")
    fun categoriesGetAll(): ResponseEntity<List<AppsCategories>> {
        return ResponseEntity(appsCategoriesService.getAllCategories(), HttpStatus.OK)
    }

    @PostMapping("/update")
    fun categoriesUpdate(@RequestBody categories: List<AppsCategories>): ResponseEntity<Void> {
        appsCategoriesService.updateCategories(categories)
        return ResponseEntity(HttpStatus.OK)
    }
}
