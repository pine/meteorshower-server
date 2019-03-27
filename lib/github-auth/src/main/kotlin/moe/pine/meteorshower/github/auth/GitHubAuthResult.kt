package moe.pine.meteorshower.github.auth

data class GitHubAuthResult(
    val accessToken: String,
    val userName: String,
    val userId: Int
)