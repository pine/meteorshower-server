package moe.pine.meteorshower.controllers

import moe.pine.meteorshower.services.auth.AuthService
import moe.pine.meteorshower.services.auth.AuthStateMismatchException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.validation.constraints.NotBlank


@Controller
class AuthController(
    private val authService: AuthService
) {
    companion object {
        private val LOGGER: Logger =
            LoggerFactory.getLogger(AuthController::class.java)
    }

    @GetMapping("/oauth2/request")
    fun request(
        @NotBlank @RequestParam("callback_url") callbackUrl: String
    ): String {
        val redirectUrl = authService.request(callbackUrl)
        return "redirect:$redirectUrl"
    }

    @GetMapping("/oauth2/verify")
    fun verify(
        @NotBlank @RequestParam("code") code: String,
        @NotBlank @RequestParam("state") state: String
    ): String {
        val verifyResult =
            try {
                authService.verify(code = code, state = state)
            } catch (_: AuthStateMismatchException) {
                // TODO
                return ""
            }

        val user =
            try {
                authService.findOrCreateUser(
                    githubId = verifyResult.githubId,
                    name = verifyResult.name
                )
            } catch (_: Exception) {
                return ""
            }

        val accessToken = authService.issueAccessToken()

        val redirectUrl = "${verifyResult.callbackUrl}?access_token=$accessToken"
        LOGGER.debug("Verification successful :: redirect-url={}", redirectUrl)

        return "redirect:$redirectUrl"
    }
}
