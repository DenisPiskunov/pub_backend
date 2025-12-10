package ru.mint.mobile.store.parser.service

import ru.mint.mobile.store.parser.repository.entity.AppsCategories

interface CategoriesService {
    fun downloadAllCategories()
    fun getAllCategories(): List<AppsCategories>
    fun updateCategories(categories: List<AppsCategories>)

}