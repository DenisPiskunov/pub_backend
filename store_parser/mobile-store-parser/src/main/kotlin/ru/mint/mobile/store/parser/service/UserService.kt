
package ru.mint.mobile.store.parser.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.mint.mobile.store.parser.service.dto.UserDetails
import ru.mint.mobile.store.parser.repository.UsersRepository
import ru.mint.mobile.store.parser.repository.entity.User

@Service
class UserService : UserDetailsService {
    @Autowired
    private lateinit var usersRepository: UsersRepository

    @Bean
    fun bcryptPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(4)
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = usersRepository.findByUsername(username!!)
        return UserDetails(user!!)
    }

    fun setUserPassword(username: String, password: String) {
        val user = usersRepository.findByUsername(username)
        if (user != null) {
            user.password = bcryptPasswordEncoder().encode(password)
            usersRepository.saveAndFlush(user)
        }
    }

    fun createUser(user: User) {
       usersRepository.saveAndFlush(user)
    }

    fun saveUser(user: User, oldUserName: String) {
        val dbUser = usersRepository.findByUsername(oldUserName)
        if (dbUser != null) {
            dbUser.username = user.username
            dbUser.roles = user.roles
            dbUser.enabled = user.enabled
            usersRepository.saveAndFlush(dbUser)
        }
    }

    fun findAllUsers(): List<User> {
        return usersRepository.findAll()
    }

    fun findUser(username: String): User? {
        return usersRepository.findByUsername(username)
    }

    fun deleteUser(username: String) {
        val user = usersRepository.findByUsername(username)
        if (user != null) {
            usersRepository.delete(user)
        }
    }
}