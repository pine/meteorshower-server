package moe.pine.meteorshower.setting.models

import java.time.LocalDateTime

data class UserOption(
    val userId: Long,
    val excludeForked: Boolean = false,
    val excludeGist: Boolean = false,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)