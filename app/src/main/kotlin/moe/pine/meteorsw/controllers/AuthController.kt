package moe.pine.meteorsw.controllers;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl
import moe.pine.meteorsw.properties.GitHubProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletResponse


@Controller
class AuthController(
    val gitHubProperties: GitHubProperties
) {
    companion object {
        val LOGGER: Logger =
            LoggerFactory.getLogger(AuthController::class.java)
    }

    @GetMapping("/oauth2/request")
    fun request(response: HttpServletResponse): String {
        response.addHeader(HttpHeaders.PRAGMA, "no-cache")
        response.addHeader(
            HttpHeaders.CACHE_CONTROL, "private, no-cache, no-store, must-revalidate")

        LOGGER.info("{}", gitHubProperties.oauth2.authorizeUrl)

        val codeUrl = AuthorizationCodeRequestUrl(
            gitHubProperties.oauth2.authorizeUrl,
            gitHubProperties.oauth2.clientId
        )
        codeUrl.setScopes(emptyList())
        codeUrl.redirectUri = "http://localhost:8080/oauth2/verify"
//        codeUrl.state = "this_is_test_state_code"
        codeUrl.set("nonce", "this_is_one_time_phrase")

        val redirectUrl = codeUrl.build()
        return "redirect:$redirectUrl"
    }

    @GetMapping("/oauth2/verify")
    fun verify() {

    }
}
