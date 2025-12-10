package ru.mint.rest.html

import kotlinx.html.*
import java.nio.charset.StandardCharsets

fun badRequestHtml(): HTML.() -> Unit = {
    head {
        title("Bad request")
        meta { charset = StandardCharsets.UTF_8.name() }
        link(href = "https://fonts.googleapis.com/css2?family=Roboto:wght@700&display=swap", rel = "stylesheet")
        styleLink("/assets/error-400.css")
    }
    body {
        style = "margin: 0"
        div("exception-body bad-request") {
            div("exception-content") {
                div("exception-title") { +"400 BAD REQUEST" }
                div("exception-detail") { +"Sent request is invalid." }
            }
        }
    }
}