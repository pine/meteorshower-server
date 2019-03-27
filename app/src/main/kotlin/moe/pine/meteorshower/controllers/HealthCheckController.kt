package moe.pine.meteorshower.controllers;

import moe.pine.meteorshower.properties.AppProperties
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody


@Controller
class HealthCheckController(
    val appProperties: AppProperties
) {
    @GetMapping("/")
    fun home(): String {
        return "redirect:${appProperties.siteUrl}"
    }

    @GetMapping("/health", produces = [MediaType.TEXT_PLAIN_VALUE])
    @ResponseBody
    fun health(): String {
        return "OK"
    }
}

