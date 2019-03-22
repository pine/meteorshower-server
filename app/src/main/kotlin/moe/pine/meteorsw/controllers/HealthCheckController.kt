package moe.pine.meteorsw.controllers;

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletResponse


@Controller
class HealthCheckController {
    companion object {
        const val HOME_URL = "https://chrome.google.com/webstore/detail/dlnjecnemdcmfbgpldflopmhifdigocb"
    }

    @GetMapping("/")
    fun home(): String {
        return String.format("redirect:%s", HOME_URL)
    }

    @GetMapping("/health", produces = [MediaType.TEXT_PLAIN_VALUE])
    @ResponseBody
    fun health(response: HttpServletResponse): String {
        response.addHeader(HttpHeaders.PRAGMA, "no-cache")
        response.addHeader(
                HttpHeaders.CACHE_CONTROL, "private, no-cache, no-store, must-revalidate")

        return "OK"
    }
}

