package ru.mint.converters

class HeadersConverter(private val reqHeaders: Set<Map.Entry<String, List<String>>>) {
    fun get(): List<String> {
        var headersList = mutableListOf<String>()
        if (reqHeaders.isEmpty()) return headersList

        reqHeaders.forEach { entry ->
            val mapKey = entry.key
            var stringifyList = entry.value.joinToString()
            headersList.add("$mapKey: $stringifyList")
        }
        return headersList
    }
}