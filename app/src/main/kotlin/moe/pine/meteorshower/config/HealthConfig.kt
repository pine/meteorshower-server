package moe.pine.meteorshower.config

import moe.pine.meteorshower.health.MySQLHealth
import moe.pine.meteorshower.health.RedisHealth
import moe.pine.meteorshower.health.repositories.HealthRepository
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@MapperScan("moe.pine.meteorshower.health.repositories")
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
class HealthConfig {
    @Bean
    fun mysqlHealth(healthRepository: HealthRepository): MySQLHealth {
        return MySQLHealth(
            healthRepository = healthRepository
        )
    }

    @Bean
    fun redisHealth(): RedisHealth {
        return RedisHealth()
    }
}
