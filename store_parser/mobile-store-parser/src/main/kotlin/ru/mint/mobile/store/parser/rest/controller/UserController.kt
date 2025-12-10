package ru.mint.mobile.store.parser.rest.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import ru.mint.mobile.store.parser.repository.entity.User
import ru.mint.mobile.store.parser.rest.domain.UserParamsBundle
import ru.mint.mobile.store.parser.service.UserService

@Controller
@RequestMapping("/user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/create")
    fun createUser(@RequestBody user: User): ResponseEntity<Void> {
        userService.createUser(user)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/getall")
    fun getAllUsers(): ResponseEntity<List<User>> {
        return ResponseEntity(userService.findAllUsers(), HttpStatus.OK)
    }


    @GetMapping("/get")
    fun getSingleUser(username: String): ResponseEntity<User> {
        val user = userService.findUser(username)
        return if (user != null) {
            ResponseEntity(user, HttpStatus.OK)
        } else {
            ResponseEntity(null, HttpStatus.OK)
        }
    }

    @PostMapping("/update")
    fun updateUser(@RequestBody userParamsBundle: UserParamsBundle): ResponseEntity<Void> {
        userService.saveUser(userParamsBundle.user!!, userParamsBundle.oldUsername!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/setpassword")
    fun setPassword(@RequestBody userParamsBundle: UserParamsBundle): ResponseEntity<Void> {
        userService.setUserPassword(userParamsBundle.username!!, userParamsBundle.password!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/delete")
    fun deleteUser(@RequestBody username: String): ResponseEntity<Void> {
        userService.deleteUser(username)
        return ResponseEntity(HttpStatus.OK)

    }

}