package ru.mint.mobile.store.parser.rest.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import ru.mint.mobile.store.parser.rest.domain.MailListBundle
import ru.mint.mobile.store.parser.service.NotificationRecipientsService

@Controller
@RequestMapping("/maillist")
class EmailSendingController {
    @Autowired
    private lateinit var notificationRecipientsService: NotificationRecipientsService

    @PostMapping("/edit")
    fun edit(@RequestBody recipientsBundle: MailListBundle): ResponseEntity<Boolean> {
        return ResponseEntity(notificationRecipientsService.editRecipients(recipientsBundle), HttpStatus.OK)
    }

    @GetMapping("/get")
    fun get(): ResponseEntity<List<String>> {
        return ResponseEntity(notificationRecipientsService.getRecipients(), HttpStatus.OK)
    }
}