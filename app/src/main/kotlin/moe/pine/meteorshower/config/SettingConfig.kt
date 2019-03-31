package moe.pine.meteorshower.config

import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Configuration
import org.springframework.session.data.redis.config.ConfigureRedisAction
import org.springframework.context.annotation.Bean



@Configuration
@MapperScan("moe.pine.meteorshower.setting")
class SettingConfig {
    @Bean
    fun configureRedisAction(): ConfigureRedisAction {
        return ConfigureRedisAction.NO_OP
    }
}