package ru.mint.service

interface PasswordGenerator {

    fun generatePassword(charsConfig : CharactersConfig): String

    data class CharactersConfig(val specialCharsCount: Long, val numbersCount: Long, val latinCharsCount: Long, val latinCapitalCharsCount: Long)
}