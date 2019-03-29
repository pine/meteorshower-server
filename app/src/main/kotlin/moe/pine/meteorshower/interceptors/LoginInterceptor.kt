package moe.pine.meteorshower.interceptors

import moe.pine.meteorshower.services.auth.IllegalAccessTokenException
import moe.pine.meteorshower.services.auth.LoginService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoginInterceptor(
    val loginService: LoginService
) : HandlerInterceptorAdapter() {
    companion object {
        private val LOGGER: Logger =
            LoggerFactory.getLogger(LoginService::class.java)
        private val BEARER_TOKEN_REGEX = "^Bearer (\\w+)$".toRegex()
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val header = request.getHeader("Authorization") ?: ""
        val matchResult = BEARER_TOKEN_REGEX.find(header)
        if (matchResult == null) {
            response.sendError(HttpStatus.BAD_REQUEST.value())
            return false
        }

        val accessToken = matchResult.groupValues[1]
        try {
            loginService.login(accessToken)
        } catch (e: IllegalAccessTokenException) {
            LOGGER.debug("Login failed :: {}", e.message)
            response.sendError(HttpStatus.UNAUTHORIZED.value())
            return false
        }

        return true
    }

}