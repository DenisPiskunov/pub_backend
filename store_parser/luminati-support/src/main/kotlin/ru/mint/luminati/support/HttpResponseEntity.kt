package ru.mint.luminati.support


data class HttpResponseEntity<out T>(val body: T, val statusCode: Int)