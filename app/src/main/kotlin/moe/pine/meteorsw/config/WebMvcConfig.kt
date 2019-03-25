package moe.pine.meteorsw.config

import moe.pine.meteorsw.interceptors.NoCacheInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    val noCacheInterceptor: NoCacheInterceptor
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry
            .addInterceptor(noCacheInterceptor)
            .addPathPatterns("/health")
            .addPathPatterns("/oauth2/**")
    }
}