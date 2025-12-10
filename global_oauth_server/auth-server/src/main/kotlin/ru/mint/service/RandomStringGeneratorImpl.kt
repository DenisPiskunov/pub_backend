package ru.mint.service

import kotlin.random.Random

class RandomStringGeneratorImpl : RandomStringGenerator {

    private val chars: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    override fun generate(length: Int): String {
        return (1..length)
            .map { Random.nextInt(0, chars.size) }
            .map(chars::get)
            .joinToString("")
    }
}