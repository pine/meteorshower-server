package moe.pine.meteorsw.github.auth

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.common.annotations.VisibleForTesting
import org.apache.commons.text.RandomStringGenerator
import org.eclipse.egit.github.core.service.UserService
import java.security.SecureRandom


class GitHubAuth {
    companion object {
        const val NONCE_LENGTH = 32
        const val STATE_LENGTH = 32
        const val GRANT_TYPE_CODE = "authorization_code"

        val DEFAULT_RANDOM_STRING_GENERATOR: RandomStringGenerator =
            {
                val random = SecureRandom.getInstanceStrong()
                val randomStringGenerator =
                    RandomStringGenerator.Builder()
                        .withinRange(
                            charArrayOf('a', 'z'),
                            charArrayOf('A', 'Z'),
                            charArrayOf('0', '9'))
                        .usingRandom(random::nextInt)
                        .build()
                randomStringGenerator
            }()
    }


    private val config: GitHubAuthConfig
    private val randomStringGenerator: RandomStringGenerator
    private val userService: UserService

    constructor(config: GitHubAuthConfig) :
        this(config, DEFAULT_RANDOM_STRING_GENERATOR, UserService())

    @VisibleForTesting
    internal constructor(
        config: GitHubAuthConfig,
        randomStringGenerator: RandomStringGenerator,
        userService: UserService = UserService()
    ) {
        this.config = config
        this.randomStringGenerator = randomStringGenerator
        this.userService = userService
    }

    fun request(): GitHubAuthRequest {
        val codeUrl = AuthorizationCodeRequestUrl(config.authorizeUrl, config.clientId)
        val state = randomStringGenerator.generate(STATE_LENGTH)
        val nonce = randomStringGenerator.generate(NONCE_LENGTH)

        codeUrl.setScopes(emptyList())
        codeUrl.redirectUri = config.callbackUrl
        codeUrl.state = state
        codeUrl.set("nonce", nonce)

        return GitHubAuthRequest(
            redirectUrl = codeUrl.build(),
            state = state
        )
    }

    fun verify(code: String): GitHubAuthResult {
        val tokenRequest = AuthorizationCodeTokenRequest(
            NetHttpTransport(),
            JacksonFactory(),
            GenericUrl(config.accessTokenUrl),
            code
        )

        // https://github.com/googleapis/google-oauth-java-client/issues/141
        tokenRequest.requestInitializer =
            HttpRequestInitializer { request ->
                request.headers["Accept"] = "application/json"
            }

        tokenRequest.grantType = GRANT_TYPE_CODE
        tokenRequest.redirectUri = config.callbackUrl
        tokenRequest.set("client_id", config.clientId)
        tokenRequest.set("client_secret", config.clientSecret)

        val tokenResponse = tokenRequest.execute()
        val accessToken = tokenResponse.accessToken

        // https://developer.github.com/v3/users/#get-the-authenticated-user
        userService.client.setOAuth2Token(accessToken)
        val user = userService.user

        return GitHubAuthResult(
            accessToken = accessToken,
            userName = user.name,
            userId = user.id
        )
    }
}