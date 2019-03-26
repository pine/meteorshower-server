package moe.pine.meteorsw.config

import moe.pine.meteorsw.github.auth.GitHubAuth
import moe.pine.meteorsw.github.auth.GitHubAuthConfig
import moe.pine.meteorsw.properties.GitHubProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(GitHubProperties::class)
class GitHubConfig {
    @Bean
    fun githubAuthConfig(
        gitHubProperties: GitHubProperties
    ): GitHubAuthConfig {
        return GitHubAuthConfig(
            callbackUrl = "http://localhost:8080/oauth2/verify",
            authorizeUrl = gitHubProperties.oauth2.authorizeUrl,
            accessTokenUrl = gitHubProperties.oauth2.accessTokenUrl,
            clientId = gitHubProperties.oauth2.clientId,
            clientSecret = gitHubProperties.oauth2.clientSecret
        )
    }

    @Bean
    fun githubAuth(
        gitHubAuthConfig: GitHubAuthConfig
    ): GitHubAuth {
        return GitHubAuth(gitHubAuthConfig)
    }
}