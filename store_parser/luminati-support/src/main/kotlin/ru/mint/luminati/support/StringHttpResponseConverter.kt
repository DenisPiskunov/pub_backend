package ru.mint.luminati.support

import org.apache.http.HttpResponse
import org.apache.http.util.EntityUtils


class StringHttpResponseConverter : HttpResponseConverter<String> {

    override fun convert(response: HttpResponse): HttpResponseEntity<String> =
            HttpResponseEntity(EntityUtils.toString(response.entity), response.statusLine.statusCode)
}