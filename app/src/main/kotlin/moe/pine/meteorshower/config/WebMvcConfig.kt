package moe.pine.meteorshower.config

import moe.pine.meteorshower.interceptors.LoginInterceptor
import moe.pine.meteorshower.interceptors.NoCacheInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    val noCacheInterceptor: NoCacheInterceptor,
    val loginInterceptor: LoginInterceptor
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry
            .addInterceptor(noCacheInterceptor)
            .addPathPatterns("/health")
            .addPathPatterns("/oauth2/**")

        registry
            .addInterceptor(loginInterceptor)
            .addPathPatterns("/api/**")
    }
}