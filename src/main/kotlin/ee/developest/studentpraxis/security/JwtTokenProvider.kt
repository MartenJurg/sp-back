package ee.developest.studentpraxis.security

import ee.developest.studentpraxis.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtTokenProvider(private val jwtConfig: JwtConfig) {

    fun getUsernameFromToken(token: String): String? {
        return getClaimFromToken(token) { obj: Claims -> obj.subject }
    }

    fun removeBearerFromToken(token: String): String {
        return token.split(" ")[1]
    }

    fun getUserIdFromToken(token: String): Long? {
        return getClaimFromToken(token) { obj: Claims -> obj["id"] }.toString().toLong()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        return getUsernameFromToken(token) == userDetails.username && !isTokenExpired(token)
    }

    fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken(token, Claims::getExpiration)
    }

    fun generateToken(user : User): String {
        val claims = mapOf("id" to user.id, "role" to user.role.toUserRoleDto())
        return doGenerateToken(claims, user.email)
    }

    private fun <T> getClaimFromToken(token: String, claimsResolver: (Claims) -> T): T {
        return claimsResolver(getAllClaimsFromToken(token))
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
                .setSigningKey(jwtConfig.secret)
                .parseClaimsJws(token)
                .body
    }

    private fun isTokenExpired(token: String): Boolean {
        return getExpirationDateFromToken(token).before(Date())
    }

    private fun doGenerateToken(claims: Map<String, Any>, subject: String): String {
        val currentTimeMillis = System.currentTimeMillis()
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date(currentTimeMillis))
                .setExpiration(Date(currentTimeMillis + jwtConfig.getDurationMillis()))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.secret)
                .compact()
    }
}