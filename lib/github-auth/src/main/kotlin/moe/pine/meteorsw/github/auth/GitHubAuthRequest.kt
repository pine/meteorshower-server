package moe.pine.meteorsw.github.auth;

data class GitHubAuthRequest(
    val redirectUrl: String,
    val state: String
)
