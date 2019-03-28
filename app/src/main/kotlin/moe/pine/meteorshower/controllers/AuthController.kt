package moe.pine.meteorshower.controllers

import moe.pine.meteorshower.services.auth.AuthAccessTokenIssueFailureException
import moe.pine.meteorshower.services.auth.AuthFlowService
import moe.pine.meteorshower.services.auth.AuthStateMismatchException
import moe.pine.meteorshower.services.auth.AuthUserSaveFailureException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletResponse
import javax.validation.constraints.NotBlank


@Controller
class AuthController(
    private val authFlowService: AuthFlowService
) {
    companion object {
        private val LOGGER: Logger =
            LoggerFactory.getLogger(AuthController::class.java)
    }

    @GetMapping("/oauth2/request")
    fun request(
        @NotBlank @RequestParam("callback_url") callbackUrl: String
    ): String {
        val redirectUrl = authFlowService.request(callbackUrl)
        return "redirect:$redirectUrl"
    }

    @GetMapping("/oauth2/verify")
    fun verify(
        @NotBlank @RequestParam("code") code: String,
        @NotBlank @RequestParam("state") state: String,
        response: HttpServletResponse
    ) {
        val verifyResult =
            try {
                authFlowService.verify(code = code, state = state)
            } catch (e: AuthStateMismatchException) {
                e.printStackTrace()

                if (e.callbackUrl.isEmpty()) {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "`state` mismatch")
                    return
                }

                val redirectUrl = UriComponentsBuilder
                    .fromHttpUrl(e.callbackUrl)
                    .queryParam("err", ErrorCodes.AUTH_STATE_MISMATCH.code)
                    .build()
                    .toUriString()
                LOGGER.debug("Verification failed :: redirect-url={}", redirectUrl)
                response.sendRedirect(redirectUrl)
                return
            }

        val user =
            try {
                authFlowService.findOrCreateUser(
                    githubId = verifyResult.githubId,
                    name = verifyResult.name
                )
            } catch (e: AuthUserSaveFailureException) {
                e.printStackTrace()

                val redirectUrl = UriComponentsBuilder
                    .fromHttpUrl(verifyResult.callbackUrl)
                    .queryParam("err", ErrorCodes.AUTH_SAVE_USER_FAILURE.code)
                    .build()
                    .toUriString()
                LOGGER.debug("Cannot save user :: redirect-url={}", redirectUrl)
                response.sendRedirect(redirectUrl)
                return
            }

        val accessToken =
            try {
                authFlowService.issueAccessToken(user)
            } catch (e: AuthAccessTokenIssueFailureException) {
                e.printStackTrace()

                val redirectUrl = UriComponentsBuilder
                    .fromHttpUrl(verifyResult.callbackUrl)
                    .queryParam("err", ErrorCodes.AUTH_ISSUE_ACCESS_TOKEN_FAILURE.code)
                    .build()
                    .toUriString()
                LOGGER.debug("Cannot issue access token :: redirect-url={}", redirectUrl)
                response.sendRedirect(redirectUrl)
                return
            }

        val redirectUrl = UriComponentsBuilder
            .fromHttpUrl(verifyResult.callbackUrl)
            .queryParam("access_token", accessToken)
            .build()
            .toUriString()
        LOGGER.debug("Verification successful :: redirect-url={}", redirectUrl)
        response.sendRedirect(redirectUrl)
    }

    @PostMapping("/oauth2/revoke")
    fun revoke(response: HttpServletResponse) {
        authFlowService.revoke()
        response.sendError(HttpStatus.NO_CONTENT.value())
    }
}
