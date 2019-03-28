package moe.pine.meteorshower.services.auth

data class AuthVerifyResult(
    val githubId: Int,
    val name: String,
    val callbackUrl: String
)
