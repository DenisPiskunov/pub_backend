package ru.mint.mobile.store.parser.service.dto

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    USER {
        override fun getAuthority(): String {
            return "ROLE_USER"
        }
    },
    ADMIN {
        override fun getAuthority(): String {
            return "ROLE_ADMIN"
        }
    }
}