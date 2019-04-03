package moe.pine.meteorshower.controllers;

import moe.pine.meteorshower.properties.AppProperties
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletResponse


@Controller
class HealthCheckController(
    val appProperties: AppProperties
) {
    @GetMapping("/")
    fun home(response: HttpServletResponse) {
        val redirectUrl = appProperties.siteUrl
        if (redirectUrl.isBlank()) {
            response.sendError(HttpStatus.NOT_FOUND.value())
            return
        }

        response.sendRedirect(redirectUrl)
    }

    @GetMapping("/health", produces = [MediaType.TEXT_PLAIN_VALUE])
    @ResponseBody
    fun health(): String {
        return "OK"
    }
}

