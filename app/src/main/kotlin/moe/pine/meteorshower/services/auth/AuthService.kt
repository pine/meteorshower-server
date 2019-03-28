package moe.pine.meteorshower.services.auth

import moe.pine.meteorshower.auth.AccessTokenGenerator
import moe.pine.meteorshower.auth.models.User
import moe.pine.meteorshower.auth.models.UserAccessToken
import moe.pine.meteorshower.auth.repositories.UserAccessTokenRepository
import moe.pine.meteorshower.auth.repositories.UserRepository
import moe.pine.meteorshower.github.auth.GitHubAuth
import moe.pine.meteorshower.sessions.AuthSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class AuthService(
    private val authSession: AuthSession,
    private val githubAuth: GitHubAuth,
    private val userRepository: UserRepository,
    private val userAccessTokenRepository: UserAccessTokenRepository,
    private val accessTokenGenerator: AccessTokenGenerator
) {
    companion object {
        private val LOGGER: Logger =
            LoggerFactory.getLogger(AuthService::class.java)
    }

    fun request(callbackUrl: String): String {
        LOGGER.debug("OAuth2 requested :: callback-url={}", callbackUrl)

        val authRequest = githubAuth.request()
        LOGGER.debug(
            "OAuth2 redirect url generated :: redirect-url={}, state={}",
            authRequest.redirectUrl,
            authRequest.state)

        authSession.callbackUrl = callbackUrl
        authSession.state = authRequest.state

        return authRequest.redirectUrl
    }

    fun verify(
        code: String,
        state: String
    ): AuthVerifyResult {
        val callbackUrl = authSession.callbackUrl
        LOGGER.debug(
            "OAuth2 verification started :: " +
                "code={}, state={}, saved-state={}, callback-url={}",
            code, state, authSession.state, callbackUrl)

        if (state != authSession.state) {
            throw AuthStateMismatchException()
        }

        val authResult = githubAuth.verify(code)
        LOGGER.debug(
            "OAuth2 verified :: user-id={}, user-name={}",
            authResult.userId,
            authResult.userName)

        return AuthVerifyResult(
            githubId = authResult.userId,
            name = authResult.userName,
            callbackUrl = callbackUrl
        )
    }

    fun findOrCreateUser(
        githubId: Int,
        name: String
    ): User {
        val existedUser = userRepository.findByGitHubId(githubId)
        if (existedUser != null) {
            LOGGER.debug(
                "User already existed :: id={}, github-id={}, name={}",
                existedUser.id ?: "-",
                existedUser.githubId,
                existedUser.name)
            return existedUser
        }

        val unsavedUser =
            User(
                githubId = githubId,
                name = name
            )
        userRepository.add(unsavedUser)

        val savedUser = userRepository.findByGitHubId(githubId)
        if (savedUser != null) {
            LOGGER.debug(
                "User created :: id={}, github-id={}, name={}",
                savedUser.id ?: "-",
                savedUser.githubId,
                savedUser.name)
            return savedUser
        }

        throw RuntimeException()
    }

    fun issueAccessToken(user: User): String {
        val userId = user.id ?: throw RuntimeException()

        val accessToken = accessTokenGenerator.generate()
        val userAccessToken =
            UserAccessToken(
                userId = userId,
                accessToken = accessToken
            )
        userAccessTokenRepository.add(userAccessToken)

        return accessToken
    }
}
