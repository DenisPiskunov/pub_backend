package ru.mint.service.dto

data class PageDTO<T>(val items: List<T>, val totalCount: Long)