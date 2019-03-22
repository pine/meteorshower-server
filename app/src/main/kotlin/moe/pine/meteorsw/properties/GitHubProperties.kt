package moe.pine.meteorsw.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("github")
class GitHubProperties(
    var oauth2: OAuth2 = OAuth2()
) {
    class OAuth2(
        var authorizeUrl: String = "",
        var accessTokenUrl: String = "",
        var clientId: String = "",
        var clientSecret: String = ""
    )
}
