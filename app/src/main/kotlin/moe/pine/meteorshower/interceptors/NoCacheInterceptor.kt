package moe.pine.meteorshower.interceptors

import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class NoCacheInterceptor : HandlerInterceptorAdapter() {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        response.addHeader(HttpHeaders.PRAGMA, "no-cache")
        response.addHeader(
            HttpHeaders.CACHE_CONTROL, "private, no-cache, no-store, must-revalidate")

        return true
    }
}