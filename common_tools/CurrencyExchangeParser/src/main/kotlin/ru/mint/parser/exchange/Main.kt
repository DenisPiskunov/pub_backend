package ru.mint.parser.exchange

import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.time.Duration
import org.springframework.web.util.UriComponentsBuilder

private val currencies = listOf("RUB", "USD", "EUR", "CAD", "AUD", "BTC", "INR", "IDR", "NZD", "PHP")
private const val mainResultHtmlClassName = "ccOutputRslt"
private const val baseUrl = "https://www.x-rates.com/calculator/?amount=1"
private const val fromParamName = "from"
private const val toParamName = "to"
private const val toCurrency = "RUB"
private const val currencyPattern = "[\\sa-zA-Z]+"
private var currenciesMap: MutableMap<String, String> = mutableMapOf()
private val log = LoggerFactory.getLogger("[Exchange parser]")

fun main() {
    val currenciesRates = loadCurrencies(currencies, toCurrency)
    currenciesRates.forEach {
        log.debug("==>> ${it.key} -> ${it.value}")
    }

}

fun restTemplate(): RestTemplate = RestTemplateBuilder()
    .setConnectTimeout(Duration.ofSeconds(10))
    .build()

fun loadCurrencies(currencies: List<String>, toCurrency: String): Map<String, String> {
    currencies.forEach { it ->
        if (it != toCurrency) {
            val rates = getRates(it)
            if (!rates.isNullOrEmpty()) {
                parseAndSaveRates(it, rates, toCurrency)
            }
        }

    }
    return currenciesMap
}

fun getRates(currency: String): List<String> {
    val urlTemplate = UriComponentsBuilder.fromHttpUrl(baseUrl)
        .queryParam(fromParamName, currency)
        .queryParam(toParamName, toCurrency)
        .encode()
        .toUriString()
    val response = restTemplate().getForEntity<String>(urlTemplate)
    return if (response.statusCode == HttpStatus.OK && !response.body.isNullOrBlank()) {
        HTMLParser.getElementsTextByClassAsList(response.body!!, mainResultHtmlClassName)
    } else {
        listOf()
    }
}

fun parseAndSaveRates(currency: String, elements: List<String>, toCurrency: String) {
    elements.forEach {
        try {
            val value = it.trim().replace(currencyPattern.toRegex(), "").toDouble()
            if (value > 0) {
                currenciesMap[currency] = "$value $toCurrency"
            }
        } catch (e: Exception) {
            log.error("===>>> Parsing error: $e")
        }
    }
}