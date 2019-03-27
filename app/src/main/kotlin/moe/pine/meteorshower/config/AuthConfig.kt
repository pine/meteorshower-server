package moe.pine.meteorshower.config

import moe.pine.meteorshower.auth.AccessTokenGenerator
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@MapperScan("moe.pine.meteorshower.auth")
class AuthConfig {
    @Bean
    fun accessTokenGenerator(): AccessTokenGenerator {
        return AccessTokenGenerator()
    }
}