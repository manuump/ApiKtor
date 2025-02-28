package domain.security

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.JWT
import io.ktor.server.auth.jwt.*
import java.sql.Date


object JWTConfig {
    private const val secret = "keyfh3081t04y1" //clave
    private const val issuer = "domain.com"
    private const val audience = "ktor_audience"
    private const val realm = "ktor_realm"
    private val algorithm = Algorithm.HMAC256(secret)


    fun generateToken(dni: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject("Authentication")
            .withClaim("dni", dni) //son específicas del usuario.
            .withClaim("time", System.currentTimeMillis()) //específicas del usuario.
            // .withExpiresAt(Date(System.currentTimeMillis() + 600000))  // Expira en 10 min
            .sign(algorithm)
    }

    fun configureAuthentication(config: JWTAuthenticationProvider.Config) {
        config.realm = realm
        config.verifier(
            JWT.require(algorithm)
                .withIssuer(issuer)
                .withAudience(audience)
                .build()
        )
        config.validate { credential ->
            if (credential.payload.getClaim("dni").asString() != null) {
                JWTPrincipal(credential.payload)
            } else null
        }
    }
}

