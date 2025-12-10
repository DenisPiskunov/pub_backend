package ru.mint.mobile.store.parser.repository.entity

import javax.persistence.*

@Table(name = "users")
@Entity
data class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
        var username: String,
        var password: String,
        var roles: String,
        var enabled: Boolean) {

    constructor(username: String, password: String, roles: String, enabled: Boolean): this(null, username, password, roles, enabled)
}