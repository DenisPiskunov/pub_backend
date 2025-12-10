package ru.mint.plugins

import io.ktor.config.*
import ru.mint.service.mail.MailTemplate
import ru.mint.service.mail.MailTemplates
import ru.mint.service.mail.MailingConfiguration
import java.util.*

class MailingConfigurationFactory {

    fun newConfig(appConfig: ApplicationConfig) : MailingConfiguration {
        val mailConfig = appConfig.config("application.mail")
        val host = mailConfig.property("host").getString()
        val port = mailConfig.property("port").getString()
        val protocol = mailConfig.property("protocol").getString()
        val username = mailConfig.property("username").getString()
        val password = mailConfig.property("password").getString()
        val mailProps = Properties().apply {
            put("mail.smtp.host", host)
            put("mail.smtp.port", port)
            put("mail.smtp.auth", mailConfig.property("properties.mail.smtp.auth").getString())
            put("mail.smtp.starttls.enable", mailConfig.property("properties.mail.smtp.starttls.enable").getString())
            put("mail.smtp.starttls.required", mailConfig.property("properties.mail.smtp.starttls.required").getString())
        }

        val masterAccountCreationTemplate = MailTemplate(
            mailConfig.property("templates.masterAccountCreation.subject").getString(),
            mailConfig.property("templates.masterAccountCreation.body").getString()
        )
        val templates = mapOf(MailTemplates.MASTER_ACCOUNT_CREATION to masterAccountCreationTemplate)
        return MailingConfiguration(host, port, protocol, username, password, mailProps, templates)
    }
}