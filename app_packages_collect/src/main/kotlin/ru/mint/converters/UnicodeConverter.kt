package ru.mint.converters

class UnicodeConverter(private val oldValue: String) {
    fun get() : String {
        var result = ""
        oldValue.forEach {
            if (it.code > 127) {
                var symbol = "\\u${Integer.toHexString(it.code).padStart(4, '0')}"
                result += symbol
            } else {
                result += it
            }
        }
        return result

    }
}