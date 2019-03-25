package moe.pine.meteorsw.controllers

import moe.pine.meteorsw.github.auth.GitHubAuth
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.validation.constraints.NotBlank


@Controller
class AuthController(
    val githubAuth: GitHubAuth
) {
    companion object {
        val LOGGER: Logger =
            LoggerFactory.getLogger(AuthController::class.java)
    }

    @GetMapping("/oauth2/request")
    fun request(): String {
        val authRequest = githubAuth.request()

        LOGGER.debug(
            "OAuth2 requested :: redirect-url={}, state={}",
            authRequest.redirectUrl,
            authRequest.state)

        return "redirect:${authRequest.redirectUrl}"

    }

    @GetMapping("/oauth2/verify")
    fun verify(
        @NotBlank @RequestParam("code") code: String,
        @NotBlank @RequestParam("state") state: String
    ) {
        LOGGER.debug("OAuth2 verification started :: code={}, state={}", code, state)

        val authResult = githubAuth.verify(code)

    }
}
