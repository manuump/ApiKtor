package domain.security

import io.ktor.utils.io.core.*
import java.security.MessageDigest

object PasswordHash :  PasswordHashInterface{

    override fun hash(pass: String): String {
        val passArr = pass.toByteArray()
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hashByte : ByteArray = messageDigest.digest(passArr)
        val hashHex = hashByte.joinToString("") { "%02x".format(it) }
        return hashHex
    }

    override fun verify(pass: String, passHash: String): Boolean {
        return hash(pass) == passHash
    }


}