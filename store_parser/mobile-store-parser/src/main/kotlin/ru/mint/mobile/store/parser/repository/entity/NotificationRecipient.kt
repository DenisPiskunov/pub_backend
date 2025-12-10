package ru.mint.mobile.store.parser.repository.entity

import javax.persistence.*


@Table(name = "notification_recipient")
@Entity
class NotificationRecipient(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null, val email: String)