package moe.pine.meteorshower.auth.models

import java.time.LocalDateTime

data class User(
    var id: Long? = null,
    var githubId: Int,
    var name: String,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null
)