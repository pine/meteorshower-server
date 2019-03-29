package moe.pine.meteorshower.controllers

import moe.pine.meteorshower.services.setting.Setting
import moe.pine.meteorshower.services.setting.SettingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
    fun load(): Setting {
        val setting = settingService.load()
        LOGGER.debug("Load settings successful :: {}", setting)

        return setting
    }

    @PostMapping("/api/settings")
    fun save(
        @RequestBody setting: Setting,
        response: HttpServletResponse
    ) {
        settingService.save(setting)
        LOGGER.debug("Save settings successful :: {}", setting)

        response.sendError(HttpStatus.NO_CONTENT.value())
    }
}