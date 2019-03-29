package moe.pine.meteorshower.services.auth

data class AuthVerifyResult(
    val githubId: Long,
    val name: String,
    val callbackUrl: String
)
