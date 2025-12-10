package ru.mint.service

import ru.mint.service.dto.ChangePasswordResult
import ru.mint.service.dto.ForgotPasswordResult
import ru.mint.service.dto.ResetPasswordResult
import java.util.UUID

interface AccountPasswordManager {

    fun forgotPassword(email: String): ForgotPasswordResult

    fun resetPassword(code: String, newPswd: String, newPswdConfirm: String) : ResetPasswordResult

    fun changePassword(accountUUID: UUID, pswd: String, pswdConfirm: String) : ChangePasswordResult
}