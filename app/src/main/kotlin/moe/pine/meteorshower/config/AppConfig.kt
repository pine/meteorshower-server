package moe.pine.meteorshower.config

import moe.pine.meteorshower.properties.AppProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.SecureRandom

@Configuration
@EnableConfigurationProperties(AppProperties::class)
class AppConfig {
    @Bean
    fun secureRandom(): SecureRandom {
        return SecureRandom.getInstance("NativePRNGNonBlocking")
    }
}