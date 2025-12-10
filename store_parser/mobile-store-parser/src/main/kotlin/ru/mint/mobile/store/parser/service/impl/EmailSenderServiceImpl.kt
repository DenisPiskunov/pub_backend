package ru.mint.mobile.store.parser.service.impl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring4.SpringTemplateEngine
import ru.mint.mobile.store.parser.repository.NotificationRecipientRepository
import ru.mint.mobile.store.parser.service.AppSettingsService
import ru.mint.mobile.store.parser.service.EmailSenderService
import ru.mint.mobile.store.parser.service.dto.ApplicationStateEmailDetails
import ru.mint.mobile.store.parser.service.dto.MailAppStatus
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Service
class EmailSenderServiceImpl : EmailSenderService {

    @Autowired
    private lateinit var notificationRecipientRepository: NotificationRecipientRepository
    @Autowired
    private lateinit var emailTemplateEngine: SpringTemplateEngine
    @Autowired
    private lateinit var mailSender: JavaMailSender
    @Autowired
    private lateinit var appSettingsService : AppSettingsService

    private val appDataTemplate = "app-data.html"
    private val appDataRemovedTemplate = "removed-app-data.html"

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(EmailSenderServiceImpl::class.java)
    }

    override fun sendAppsStateInfo(details: List<ApplicationStateEmailDetails>, messageSubject: String, mailAppStatus: MailAppStatus) {
        val templateName = when (mailAppStatus) {
            MailAppStatus.REMOVED -> {
                appDataRemovedTemplate
            }
            MailAppStatus.ADDED, MailAppStatus.CHANGED -> {
                appDataTemplate
            }
        }
        if (details.isNotEmpty()) {
            val ctx = Context().apply {
                setVariable("details", details)
            }
            val sender = appSettingsService.getSenderEmail()
            val body = emailTemplateEngine.process(templateName, ctx)
            val recipients = notificationRecipientRepository.findAll().map { it.email }
            sendMsg(sender, body, messageSubject, recipients)
        }
        else {logger.debug("Emails list is empty. Nothing to send.")
        }
    }

    override fun sendMessage(body: String, messageSubject: String) {
        val sender = appSettingsService.getSenderEmail()
        val recipients = mutableListOf<String>()
        recipients.add(appSettingsService.getAdminEmail())
        sendMsg(sender, body, messageSubject, recipients)
    }

    private fun sendMsg(sender: String, msg: String, subject: String, emails: List<String>) {
        val mimeMessage = mailSender.createMimeMessage()
        val message = MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name())
        message.setSubject(subject)
        message.setFrom(sender)
        message.setTo(emails.toTypedArray())
        message.setText(msg, true)
        try {
            logger.debug("Trying to send emails to : $emails")
            mailSender.send(mimeMessage)
        } catch (e: MailException) {
            logger.error("An error occurred during emails sending.", e)
        }
    }
}