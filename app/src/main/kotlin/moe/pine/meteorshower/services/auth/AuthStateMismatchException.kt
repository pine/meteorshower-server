package moe.pine.meteorshower.services.auth

class AuthStateMismatchException(
    val callbackUrl: String = ""
) : RuntimeException("`state` mismatch")
