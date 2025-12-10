package ru.mint.mobile.store.parser.service

import ru.mint.mobile.store.parser.rest.domain.MailListBundle

interface NotificationRecipientsService {
    fun getRecipients(): List<String>
    fun editRecipients(recipientsBundle: MailListBundle): Boolean
}