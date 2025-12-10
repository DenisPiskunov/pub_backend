package ru.mint.rest.html

import kotlinx.html.*
import java.nio.charset.StandardCharsets

fun successHtml(): HTML.() -> Unit = {
    head {
        title("Success")
        meta { charset = StandardCharsets.UTF_8.name() }
        link(href = "https://fonts.googleapis.com/css2?family=Roboto:wght@700&display=swap", rel = "stylesheet")
        styleLink("/assets/success.css")
    }
    body {
        style = "margin: 0"
        div("exception-body success") {
            div("exception-content") {
                div("exception-title") { +"SUCCESS" }
                div("exception-detail") { +"Request successfully sent." }
            }
        }
    }
}