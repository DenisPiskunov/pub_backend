package ru.mint.mobile.store.parser.rest.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/auth")
class AuthController {

    data class UserInfo(val token: String?, val username: String?, val userRoles: MutableList<String>?)

    //TODO: Uncomment this method for remote-server deployment
//    @PostMapping("/login")
//    fun login(@RequestParam result: String?): ResponseEntity<UserInfo> {
//        var userRoles = mutableListOf<String>()
//        var username = ""
//        return if (result == "ok") {
//            val authentication = SecurityContextHolder.getContext().authentication
//            authentication.authorities.forEach{ item ->
//                userRoles.add(item.authority)
//            }
//            username = authentication.name
//            ResponseEntity(UserInfo("successful", username, userRoles), HttpStatus.OK)
//        }
//        else {
//            ResponseEntity(UserInfo("error", username, userRoles), HttpStatus.OK)
//        }
//    }

    //TODO: Uncomment this method for local-server deployment and testing client-side
    @PostMapping("/login")
    fun login(): ResponseEntity<UserInfo> {
        var userRoles = mutableListOf<String>()
        var username = "test"
        userRoles.add("ROLE_ADMIN")
        return ResponseEntity(UserInfo("successful", username, userRoles), HttpStatus.OK)
    }


    @PostMapping("/logout")
    fun logout() {
        SecurityContextHolder.getContext().authentication.isAuthenticated = false
    }
}