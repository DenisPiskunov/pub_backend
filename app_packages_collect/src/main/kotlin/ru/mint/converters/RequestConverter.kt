package ru.mint.converters

import io.ktor.server.plugins.*
import io.ktor.server.request.*
import ru.mint.models.RequestData

class RequestConverter(private val request: ApplicationRequest) {
    private fun getHeaders() : List<String> {
        return HeadersConverter(request.headers.entries()).get()
    }

    private fun getCookies() : List<String> {
        return CookiesConverter(request.cookies.rawCookies).get()
    }

    fun getRequestData() : RequestData {
        return RequestData(
            headers = getHeaders(),
            cookies = getCookies(),
            version = request.origin.version,
            localHost = request.origin.localHost,
            serverHost = request.origin.serverHost,
            localPort = request.origin.localPort,
            serverPort = request.origin.serverPort,
            localAddress = request.origin.localAddress,
            remoteAddress = request.origin.remoteAddress,
            remoteHost = request.origin.remoteHost,
            remotePort = request.origin.remotePort,
            uri = request.origin.uri
        )
    }
}