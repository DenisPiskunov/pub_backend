package ru.mint.plugins

import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryptoUtils {

    private fun hashString(type: String, input: String): ByteArray {
        return MessageDigest.getInstance(type).digest(input.toByteArray(Charsets.UTF_8))
    }

    fun decrypt(cipherText: String, key: String, iv: String): String {
        try {
            val algorithm = "AES/CBC/PKCS5Padding"

            val key = SecretKeySpec(hashString("SHA-256", key), "AES")
            val iv = IvParameterSpec(hashString("MD5", iv))

            val cipher = Cipher.getInstance(algorithm)
            cipher.init(Cipher.DECRYPT_MODE, key, iv)
            val plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText))
            return String(plainText)
        } catch (e: java.lang.Exception) {
            return if (e.message == null)
                "error occurred during decrypt payload"
            else
                e.message.toString()
        }
    }
}


