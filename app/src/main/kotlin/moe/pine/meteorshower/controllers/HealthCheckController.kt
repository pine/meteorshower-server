package moe.pine.meteorshower.controllers;

import moe.pine.meteorshower.health.Health
import moe.pine.meteorshower.properties.AppProperties
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletResponse


@Controller
class HealthCheckController(
    val appProperties: AppProperties,
    val healths: List<Health>
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

    @GetMapping("/health")
    fun health(response: HttpServletResponse) {
        val alive = healths.all { it.alive() }
        if (alive) {
            response.writer.write(HttpStatus.OK.reasonPhrase)
            return
        }

        response.sendError(
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            HttpStatus.SERVICE_UNAVAILABLE.reasonPhrase)
    }
}

