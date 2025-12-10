package ru.mint.service.mail

import java.util.*

data class MailingConfiguration(
    val host: String,
    val port: String,
    val protocol: String,
    val username: String,
    val password: String,
    val mailProps: Properties,
    val templates: Map<MailTemplates, MailTemplate>,
)