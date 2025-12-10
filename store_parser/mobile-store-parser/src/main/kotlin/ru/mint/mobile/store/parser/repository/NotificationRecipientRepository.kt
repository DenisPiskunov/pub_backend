package ru.mint.mobile.store.parser.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.mint.mobile.store.parser.repository.entity.NotificationRecipient


interface NotificationRecipientRepository : JpaRepository<NotificationRecipient, Long> {
    fun findOneByEmail(email: String): NotificationRecipient
    fun existsByEmail(email: String): Boolean
}