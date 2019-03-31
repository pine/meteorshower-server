package moe.pine.meteorshower.config

import moe.pine.meteorshower.auth.AccessTokenGenerator
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.SecureRandom

@Configuration
@MapperScan("moe.pine.meteorshower.auth")
class AuthConfig {
    @Bean
    fun accessTokenGenerator(secureRandom: SecureRandom): AccessTokenGenerator {
        return AccessTokenGenerator(secureRandom)
    }
}