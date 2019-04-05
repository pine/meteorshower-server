package moe.pine.meteorshower.health

import org.springframework.data.redis.core.RedisTemplate

class RedisHealth(
    private val redisTemplate: RedisTemplate<String, String>
) : Health {
    override fun alive(): Boolean {
        val ping = redisTemplate.execute { conn -> conn.ping() ?: "" }
        return ping == "PONG"
    }
}
