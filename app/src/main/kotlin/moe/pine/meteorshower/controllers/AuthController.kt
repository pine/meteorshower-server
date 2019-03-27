package moe.pine.meteorshower.controllers

import moe.pine.meteorshower.auth.AccessTokenGenerator
import moe.pine.meteorshower.auth.repositories.UserRepository
import moe.pine.meteorshower.github.auth.GitHubAuth
import moe.pine.meteorshower.sessions.AuthSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.validation.constraints.NotBlank


@Controller
class AuthController(
    val authSession: AuthSession,
    val githubAuth: GitHubAuth,
    val accessTokenGenerator: AccessTokenGenerator,
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    val userRepository: UserRepository
) {
    companion object {
        val LOGGER: Logger =
            LoggerFactory.getLogger(AuthController::class.java)
    }

    @GetMapping("/oauth2/request")
    fun request(
        @NotBlank @RequestParam("callback_url") callbackUrl: String
    ): String {
        LOGGER.debug("OAuth2 requested :: callback-url={}", callbackUrl)

        val authRequest = githubAuth.request()
        LOGGER.debug(
            "OAuth2 redirect url generated :: redirect-url={}, state={}",
            authRequest.redirectUrl,
            authRequest.state)

        authSession.callbackUrl = callbackUrl
        authSession.state = authRequest.state

        return "redirect:${authRequest.redirectUrl}"
    }

    @GetMapping("/oauth2/verify")
    fun verify(
        @NotBlank @RequestParam("code") code: String,
        @NotBlank @RequestParam("state") state: String
    ): String {
        val callbackUrl = authSession.callbackUrl
        LOGGER.debug(
            "OAuth2 verification started :: " +
                "code={}, state={}, saved-state={}, callback-url={}",
            code, state, authSession.state, callbackUrl)

        val authResult = githubAuth.verify(code)
        LOGGER.debug(
            "OAuth2 verified :: user-id={}, user-name={}",
            authResult.userId,
            authResult.userName)

        val user = userRepository.findByTwitterId(authResult.userId)
        /*
        LOGGER.debug(
            "OAuth2 verified :: user-id={}, user-name={}",
            authResult.userId,
            authResult.userName)
        */
        // val accessToken = accessTokenGenerator.generate()


        return "redirect:$callbackUrl"
    }
}
