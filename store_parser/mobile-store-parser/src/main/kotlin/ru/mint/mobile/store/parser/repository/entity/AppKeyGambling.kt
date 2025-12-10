package ru.mint.mobile.store.parser.repository.entity

import java.time.LocalDateTime
import javax.persistence.*

@Table(name = "app_key_gambling")
@Entity
data class AppKeyGambling(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
        @Column(name = "key") val key: String,
        @Column(name = "created_date") val creationDate: LocalDateTime) {

    constructor(key: String) : this(null, key, LocalDateTime.now())
}