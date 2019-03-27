package moe.pine.meteorshower.github.auth

data class GitHubAuthConfig(
    val callbackUrl: String,
    val authorizeUrl: String,
    val accessTokenUrl: String,
    val clientId: String,
    val clientSecret: String
)