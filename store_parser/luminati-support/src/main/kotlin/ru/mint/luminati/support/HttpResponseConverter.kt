package ru.mint.luminati.support

import org.apache.http.HttpResponse


interface HttpResponseConverter<out T> {

    fun convert(response: HttpResponse): HttpResponseEntity<T>

}