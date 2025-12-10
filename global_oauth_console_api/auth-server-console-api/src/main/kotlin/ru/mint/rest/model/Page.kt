package ru.mint.rest.model

import kotlinx.serialization.Serializable

@Serializable
class Page<T>(val items:List<T>, val totalCount: Long)