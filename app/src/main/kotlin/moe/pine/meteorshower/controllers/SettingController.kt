package moe.pine.meteorshower.controllers

import moe.pine.meteorshower.scoped.Authenticated
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SettingController(
    val authenticated: Authenticated
) {
    companion object {
        private val LOGGER: Logger =
            LoggerFactory.getLogger(SettingController::class.java)
    }

    @GetMapping("/api/settings")
    fun find(): String {
        val user = authenticated.requestUser()

        return ""
    }

    @PostMapping("/api/settings")
    fun save(): String {
        return ""
    }
}