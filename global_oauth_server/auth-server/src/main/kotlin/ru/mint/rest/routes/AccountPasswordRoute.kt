package ru.mint.rest.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.mint.rest.html.accountResetPasswordHtml
import ru.mint.rest.html.badRequestHtml
import ru.mint.rest.html.notFoundHtml
import ru.mint.rest.html.successHtml
import ru.mint.rest.model.ChangePasswordRequest
import ru.mint.rest.model.ForgotPasswordRequest
import ru.mint.service.AccountPasswordManager
import ru.mint.service.AccountResetPasswordSecretCodeChecker
import ru.mint.service.dto.AccountResetPasswordSecretCodeCheckResult
import ru.mint.service.dto.ChangePasswordResult
import ru.mint.service.dto.ForgotPasswordResult.ResetPasswordURLCreated
import ru.mint.service.dto.ResetPasswordResult.*
import java.util.*

fun Route.accountPassword() {

    val accountPasswordManager by closestDI().instance<AccountPasswordManager>()
    val accountResetPasswordSecretCodeChecker by closestDI().instance<AccountResetPasswordSecretCodeChecker>()

    val resetPwdSecretCodeRouteParam = "resetPwdSecretCode"

    post("forgot-pswd") {
        val request = call.receive<ForgotPasswordRequest>()
        when (accountPasswordManager.forgotPassword(request.email)) {
            ResetPasswordURLCreated -> call.respond(HttpStatusCode.OK)
            else -> call.respond(HttpStatusCode.BadRequest)
        }
    }

    get("reset-pswd/{$resetPwdSecretCodeRouteParam}") {
        val secretCode = call.parameters[resetPwdSecretCodeRouteParam].toString()
        when (accountResetPasswordSecretCodeChecker.check(secretCode)) {
            is AccountResetPasswordSecretCodeCheckResult.Ok -> call.respondHtml(HttpStatusCode.OK, accountResetPasswordHtml())
            else -> call.respondHtml(HttpStatusCode.NotFound, notFoundHtml())
        }
    }

    post("reset-pswd/{$resetPwdSecretCodeRouteParam}") {
        val formParams = call.receiveParameters()
        val newPswd = formParams["restorePass"].toString()
        val newPswdConfirm = formParams["restoreConfPass"].toString()
        val secretCode = call.parameters[resetPwdSecretCodeRouteParam].toString()
        when (accountPasswordManager.resetPassword(secretCode, newPswd, newPswdConfirm)) {
            PasswordWasChanged -> call.respondHtml(HttpStatusCode.OK, successHtml())
            PasswordConfirmationFailed -> call.respondHtml(HttpStatusCode.BadRequest, badRequestHtml())
            is InvalidSecretCode -> call.respondHtml(HttpStatusCode.NotFound, notFoundHtml())
        }
    }

    authenticate {
        post("change-pswd") {
            val accountUUID = UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("uuid").asString())
            val request = call.receive<ChangePasswordRequest>()
            val changePswdResult = accountPasswordManager.changePassword(accountUUID, request.password, request.passwordConfirm)
            if (ChangePasswordResult.PasswordWasChanged == changePswdResult) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.BadRequest)
        }
    }


}