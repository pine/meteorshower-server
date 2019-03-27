package moe.pine.meteorshower.github.auth;

data class GitHubAuthRequest(
    val redirectUrl: String,
    val state: String
)
