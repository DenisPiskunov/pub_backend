package ru.mint.service.mail

import java.util.*

data class MailingConfiguration(
    val protocol: String,
    val username: String,
    val password: String,
    val props: Properties,
    val templates: Map<MailTemplates, MailTemplate>
)