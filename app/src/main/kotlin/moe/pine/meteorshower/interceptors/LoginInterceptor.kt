package moe.pine.meteorshower.interceptors

import moe.pine.meteorshower.services.auth.LoginService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoginInterceptor(
    val loginService: LoginService
) : HandlerInterceptorAdapter() {
    companion object {
        val BEARER_TOKEN_REGEX = "^Bearer (\\w+)$".toRegex()
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val header = request.getHeader("Authorization")
        val matchResult = BEARER_TOKEN_REGEX.find(header)
            ?: return false

        val accessToken = matchResult.groupValues[1]


        return true
    }

}