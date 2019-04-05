package moe.pine.meteorshower.controllers

import moe.pine.meteorshower.services.setting.Setting
import moe.pine.meteorshower.services.setting.SettingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
class SettingController(
    val settingService: SettingService
) {
    companion object {
        private val LOGGER: Logger =
            LoggerFactory.getLogger(SettingController::class.java)
    }

    @GetMapping("/api/settings")
    fun get(): Setting {
        val setting = settingService.get()
        LOGGER.debug("Load settings successful :: {}", setting)

        return setting
    }

    @PostMapping("/api/settings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun save(
        @RequestBody @Validated setting: Setting,
        response: HttpServletResponse
    ) {
        settingService.save(setting)
        LOGGER.debug("Save settings successful :: {}", setting)
    }
}