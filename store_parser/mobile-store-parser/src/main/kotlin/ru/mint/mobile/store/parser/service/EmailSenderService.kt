package ru.mint.mobile.store.parser.service

import ru.mint.mobile.store.parser.service.dto.ApplicationStateEmailDetails
import ru.mint.mobile.store.parser.service.dto.MailAppStatus
import java.awt.im.InputSubset


interface EmailSenderService {

    /**
     * sends emails with information about applications current state
     */
    fun sendAppsStateInfo(details: List<ApplicationStateEmailDetails>, messageSubject: String, mailAppStatus: MailAppStatus = MailAppStatus.ADDED)
    fun sendMessage(body: String, messageSubject: String)
}