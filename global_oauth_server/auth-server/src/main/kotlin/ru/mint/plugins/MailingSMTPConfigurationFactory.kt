package ru.mint.plugins

import io.ktor.config.*
import ru.mint.service.mail.MailTemplate
import ru.mint.service.mail.MailTemplates
import ru.mint.service.mail.MailingConfiguration
import java.util.*

class MailingSMTPConfigurationFactory {

    private val transportProtocol = "smtp"

    fun newConfig(appConfig: ApplicationConfig): MailingConfiguration {
        val mailConfig = appConfig.config("application.mail")
        val mailSMTPConfig = mailConfig.config(transportProtocol)

        val host = mailSMTPConfig.property("host").getString()
        val port = mailSMTPConfig.property("port").getString()
        val username = mailSMTPConfig.property("username").getString()
        val password = mailSMTPConfig.property("password").getString()

        val mailSMTPPropertiesConfig = mailSMTPConfig.config("properties")

        val mailProps = Properties().apply {
            put("mail.smtp.host", host)
            put("mail.smtp.port", port)
            put("mail.smtp.auth", mailSMTPPropertiesConfig.property("auth").getString())
            put("mail.smtp.starttls.enable", mailSMTPPropertiesConfig.property("starttls.enable").getString())
            put("mail.smtp.starttls.required", mailSMTPPropertiesConfig.property("starttls.required").getString())
        }

        val resetPswdTemplate = MailTemplate(
            mailConfig.property("templates.resetPassword.subject").getString(),
            mailConfig.property("templates.resetPassword.body").getString()
        )
        val templates = mapOf(MailTemplates.RESET_PASSWORD to resetPswdTemplate)
        return MailingConfiguration(transportProtocol, username, password, mailProps, templates)
    }
}