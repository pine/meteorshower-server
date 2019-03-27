package moe.pine.meteorshower.auth.models

import java.time.LocalDateTime

data class UserAccessToken(
    var userId: Long,
    var accessToken: String,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null
)