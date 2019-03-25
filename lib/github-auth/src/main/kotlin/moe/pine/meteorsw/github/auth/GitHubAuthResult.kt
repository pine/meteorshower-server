package moe.pine.meteorsw.github.auth

data class GitHubAuthResult(
    val accessToken: String,
    val userName: String,
    val userId: Int
)