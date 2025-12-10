package ru.mint.rest.html

import kotlinx.html.*
import java.nio.charset.StandardCharsets

fun notFoundHtml(): HTML.() -> Unit = {
    head {
        title("Not found")
        meta { charset = StandardCharsets.UTF_8.name() }
        link(href = "https://fonts.googleapis.com/css2?family=Roboto:wght@700&display=swap", rel = "stylesheet")
        styleLink("/assets/error-404.css")
    }
    body {
        style = "margin: 0"
        div("exception-body notfound") {
            div("exception-content") {
                div("exception-title") { +"404 NOT FOUND" }
                div("exception-detail") { +"Requested resource is not found." }
            }
        }
    }
}