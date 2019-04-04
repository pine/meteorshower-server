package moe.pine.meteorshower.health

import moe.pine.meteorshower.health.repositories.HealthRepository

class MySQLHealth(
    private val healthRepository: HealthRepository
) : Health {
    override fun alive(): Boolean {
        return healthRepository.alive() == 1L
    }
}
