package ru.mint.mobile.store.parser.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import ru.mint.mobile.store.parser.repository.entity.User

@Transactional(readOnly = true)
interface UsersRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String) : User?
}