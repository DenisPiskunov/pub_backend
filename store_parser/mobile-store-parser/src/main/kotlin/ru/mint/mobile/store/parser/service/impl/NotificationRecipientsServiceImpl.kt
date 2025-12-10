package ru.mint.mobile.store.parser.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.mint.mobile.store.parser.repository.NotificationRecipientRepository
import ru.mint.mobile.store.parser.repository.entity.NotificationRecipient
import ru.mint.mobile.store.parser.rest.domain.MailListBundle
import ru.mint.mobile.store.parser.service.NotificationRecipientsService
import ru.mint.mobile.store.parser.service.dto.EditMode

@Service
class NotificationRecipientsServiceImpl : NotificationRecipientsService {

    @Autowired
    private lateinit var notificationRecipientRepository: NotificationRecipientRepository

    override fun getRecipients(): List<String> {
        return notificationRecipientRepository.findAll().map { it.email }
    }

    override fun editRecipients(recipientsBundle: MailListBundle): Boolean {
        val editMode: EditMode = try {
            EditMode.valueOf(recipientsBundle.editMode)
        } catch (e: Exception) {
            return false
        }

        when (editMode) {
            EditMode.ADD -> {
                recipientsBundle.mailList.forEach { email ->
                    if (!notificationRecipientRepository.existsByEmail(email)) {
                        val notificationRecipient = NotificationRecipient(email = email)
                        notificationRecipientRepository.save(notificationRecipient)
                    }
                }
            }

            EditMode.DELETE -> {
                recipientsBundle.mailList.forEach { email ->
                    val id = notificationRecipientRepository.findOneByEmail(email).id
                    notificationRecipientRepository.delete(id)
                }
            }
        }
        return true
    }
}