package ee.developest.studentpraxis.security

import io.jsonwebtoken.ExpiredJwtException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtRequestFilter(private val jwtTokenProvider: JwtTokenProvider,
                       private val authUserDetailsService: AuthUserDetailsService) : OncePerRequestFilter() {
    companion object {
        const val BEARER: String = "Bearer"
        val logger = LoggerFactory.getLogger(JwtRequestFilter::class.java.simpleName)
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = getToken(request)

        if (token == null) {
            filterChain.doFilter(request, response)
            return
        }
        val username = getUsername(token)
        if (username == null) {
            filterChain.doFilter(request, response)
            return
        }
        if (SecurityContextHolder.getContext().authentication == null) {
            val userDetails: UserDetails = authUserDetailsService.loadUserByUsername(username)
            if (jwtTokenProvider.validateToken(token, userDetails)) {
                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun getUsername(jwtToken: String): String? {
        try {
            return jwtTokenProvider.getUsernameFromToken(jwtToken)
        } catch (e: IllegalArgumentException) {
            logger.error("Unable to get JWT Token", e)
        } catch (e: ExpiredJwtException) {
            logger.error("JWT Token has expired", e)
        }
        return null
    }

    private fun getToken(request: HttpServletRequest): String? {
        val requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        return if (requestTokenHeader == null || !requestTokenHeader.startsWith(BEARER)) {
            null
        } else requestTokenHeader.substring(7)
    }
}