package ru.mint.mobile.store.parser.service.dto

import org.springframework.security.core.userdetails.UserDetails
import ru.mint.mobile.store.parser.repository.entity.User

class UserDetails(val user: User) : UserDetails {

    override fun getAuthorities(): MutableList<Role> {
        val roles = mutableListOf<Role>()
        val userRoles: String? = user.roles
        if (userRoles.isNullOrBlank()) {
            roles.add(Role.USER)
        } else {
            val userRolesList = userRoles!!.split(delimiters = *arrayOf(","), ignoreCase = true, limit = 0)
            userRolesList.forEach { item ->
                val role: Role = try {
                    Role.valueOf(item)
                }
                catch (e: Exception) {
                    Role.USER
                }
                roles.add(role)
            }
        }
        return roles
    }

    override fun isEnabled(): Boolean {
        return user.enabled
    }

    override fun getUsername(): String {
        return user.username
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }
}