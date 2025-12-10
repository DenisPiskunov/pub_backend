package ru.mint.service.mail

interface MailService {

    fun sendResetPasswordEmail(recipient: String, resetPswdURL: String)
}