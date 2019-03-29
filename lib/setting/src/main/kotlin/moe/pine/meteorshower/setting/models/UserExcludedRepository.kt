package moe.pine.meteorshower.setting.models

import java.time.LocalDateTime

data class UserExcludedRepository(
    val id: Long? = null,
    val userId: Long,
    val owner: String,
    val name: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)