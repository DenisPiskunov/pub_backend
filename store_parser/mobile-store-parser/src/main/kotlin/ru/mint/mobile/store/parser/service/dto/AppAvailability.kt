package ru.mint.mobile.store.parser.service.dto

class AppAvailability (
        var available_in : List<String>? = null,
        var not_available_in : List<String>? = null,
        var availability_unknown : List<String>? = null,
        var package_name : String? = null
)