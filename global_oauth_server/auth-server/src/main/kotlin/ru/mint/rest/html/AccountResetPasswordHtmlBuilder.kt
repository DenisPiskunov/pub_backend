package ru.mint.rest.html

import kotlinx.html.*
import java.nio.charset.StandardCharsets

fun accountResetPasswordHtml(): HTML.() -> Unit = {
    head {
        title("Reset password")
        meta { charset = StandardCharsets.UTF_8.name() }
        link(href = "https://fonts.gstatic.com", rel = "preconnect")
        link(href = "https://fonts.googleapis.com/css2?family=Nunito:wght@400&display=swap", rel = "stylesheet")
        styleLink("/assets/reset-password.css")
        script(src = "/assets/reset-password.js") {}
    }
    body {
        form(classes = "restore-form", method = FormMethod.post) {
            name = "restore"
            onSubmit = "submitForm(event)"
            h2(classes = "restore-title") {
                +"Reset password"
            }
            p(classes = "restore-text") {
                +"Enter your new password and confirm it"
            }
            input(InputType.password, name = "restorePass") {
                placeholder = "New password"
            }
            input(InputType.password, name = "restoreConfPass") {
                placeholder = "Confirm new password"
            }
            div {
                id = "errorMessage"
                style = "display: none"
            }
            button {
                +"OK"
            }
        }
    }
}