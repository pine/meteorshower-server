package moe.pine.meteorshower.auth.models

import java.time.LocalDateTime

data class User(
    val id: Long? = null,
    val githubId: Long,
    val name: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    fun requestId(): Long {
        return id ?: throw IllegalStateException("`id` should not be null")
    }
}