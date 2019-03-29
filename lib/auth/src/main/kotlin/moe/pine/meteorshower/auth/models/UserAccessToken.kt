package moe.pine.meteorshower.auth.models

import java.time.LocalDateTime

data class UserAccessToken(
    val userId: Long,
    val accessToken: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)