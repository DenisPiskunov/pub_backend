package ru.mint.converters

class CookiesConverter(private val rawCookies: Map<String, String>) {
    fun get(): List<String> {
        var cookiesList = mutableListOf<String>()
        if (rawCookies.isEmpty()) return cookiesList

        rawCookies.keys.forEach { key ->
            val value = rawCookies[key]
            cookiesList.add("$key: $value")
        }
        return cookiesList
    }
}