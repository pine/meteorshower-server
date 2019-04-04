package moe.pine.meteorshower.health

class RedisHealth : Health {
    override fun alive(): Boolean {
        return true
    }
}
