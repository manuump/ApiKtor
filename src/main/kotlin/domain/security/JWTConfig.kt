package domain.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JWTConfig {
    private const val SECRET = "u4hr78139gp43"
    private const val ISSUER = "domain.com"
    private const val VALIDITY_IN_MS = 36_000_00 * 24 // 24 horas

    val algorithm = Algorithm.HMAC256(SECRET)

    val verifier = JWT.require(algorithm)
        .withIssuer(ISSUER)
        .build()

    fun generateToken(userId: Int, username: String): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withClaim("userId", userId)
            .withClaim("username", username)
            .withExpiresAt(Date(System.currentTimeMillis() + VALIDITY_IN_MS))
            .sign(algorithm)
    }
}