package ru.mint.service.mail

interface MailService {

    fun sendNewMasterAccountEmail(recipient: String, password: String)
}