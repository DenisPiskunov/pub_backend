package ru.mint.service.mail

import org.slf4j.LoggerFactory
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class MailServiceImpl(private val mailingConfig: MailingConfiguration) : MailService {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val mailSession: Session = Session.getInstance(mailingConfig.props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(mailingConfig.username, mailingConfig.password)
        }
    })

    override fun sendResetPasswordEmail(recipient: String, resetPswdURL: String) {
        val msgTemplate = mailingConfig.templates[MailTemplates.RESET_PASSWORD]!!
        val msgBody = String.format(msgTemplate.body, resetPswdURL)
        val msgSubject = msgTemplate.subject
        sendMessage(newMessage(msgSubject, msgBody, recipient))
    }

    private fun newMessage(subject: String, body: String, recipient: String) = MimeMessage(mailSession)
        .apply {
            setFrom(InternetAddress(mailingConfig.username))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient, false))
            setText(body)
            setSubject(subject)
            sentDate = Date()
        }

    private fun sendMessage(message: MimeMessage) {
        try {
            val smtpTransport = mailSession.getTransport(mailingConfig.protocol)
            smtpTransport.connect()
            smtpTransport.sendMessage(message, message.allRecipients)
            smtpTransport.close()
        } catch (e: MessagingException) {
            logger.error("An error occurred during sending of email", e)
        }
    }
}