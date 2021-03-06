package moe.pine.meteorshower.services.auth

import moe.pine.meteorshower.auth.repositories.UserAccessTokenRepository
import moe.pine.meteorshower.auth.repositories.UserRepository
import moe.pine.meteorshower.scoped.Authenticated
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class LoginService(
    val authenticated: Authenticated,
    val userRepository: UserRepository,
    val userAccessTokenRepository: UserAccessTokenRepository
) {
    companion object {
        private val LOGGER: Logger =
            LoggerFactory.getLogger(LoginService::class.java)
    }

    fun login(accessToken: String) {
        if (accessToken.isEmpty()) {
            throw IllegalAccessTokenException("`accessToken` should not be empty")
        }

        val userAccessToken =
            userAccessTokenRepository.findByAccessToken(accessToken)
                ?: throw IllegalAccessTokenException(
                    "The access token is not registered :: access-token=$accessToken")

        val user = userRepository.findById(userAccessToken.userId)
            ?: throw IllegalAccessTokenException(
                "The user not found :: access-token=$accessToken, user-id=${userAccessToken.userId}")

        LOGGER.debug(
            "Login successful :: access-token={}, user-id={}, github-id={}, last-accessed-at={}",
            accessToken, user.id, user.githubId, userAccessToken.lastAccessedAt)

        val updatedUserAccessToken =
            userAccessToken.copy(
                lastAccessedAt = LocalDateTime.now()
            )
        val updatedCount = userAccessTokenRepository.update(updatedUserAccessToken)
        assert(updatedCount == 1L)

        authenticated.user = user
        authenticated.userAccessToken = userAccessToken
    }
}