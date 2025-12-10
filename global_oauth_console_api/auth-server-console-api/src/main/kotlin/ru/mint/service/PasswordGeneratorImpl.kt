package ru.mint.service

import ru.mint.service.PasswordGenerator.CharactersConfig
import java.security.SecureRandom
import java.util.stream.Collectors.toList
import java.util.stream.Stream
import java.util.stream.Stream.concat

class PasswordGeneratorImpl : PasswordGenerator {

    override fun generatePassword(charsConfig: CharactersConfig): String {
        val passwordChars = concat(
            concat(getRandomSpecialChars(charsConfig.specialCharsCount), getRandomNumbers(charsConfig.numbersCount)),
            concat(getRandomLatinLetters(charsConfig.latinCharsCount), getRandomLatinCapitalLetters(charsConfig.latinCapitalCharsCount))
        )
            .collect(toList())
        passwordChars.shuffle()
        return passwordChars.joinToString(separator = "")
    }

    private fun getRandomCharacters(count: Long, charCodeLowerBound: Int, charCodeUpperBound: Int): Stream<Char> {
        return SecureRandom().ints(count, charCodeLowerBound, charCodeUpperBound).mapToObj { data: Int -> data.toChar() }

    }

    private fun getRandomLatinLetters(count: Long) = getRandomCharacters(count, 97, 123)

    private fun getRandomLatinCapitalLetters(count: Long) = getRandomCharacters(count, 65, 91)

    private fun getRandomNumbers(count: Long) = getRandomCharacters(count, 48, 58)

    private fun getRandomSpecialChars(count: Long) = getRandomCharacters(count, 33, 48)
}