package moe.pine.meteorshower

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.net.URI

@SpringBootApplication
class App

fun main(args: Array<String>) {
    val port = System.getenv("PORT") ?: ""
    if (port.isNotEmpty()) {
        System.setProperty("server.port", port)
    }

    val redisUrl = System.getenv("REDIS_URL") ?: ""
    if (redisUrl.isNotEmpty()) {
        val parsedUri = URI.create(redisUrl)
        System.setProperty("spring.redis.host", parsedUri.host)
        System.setProperty("spring.redis.password", parsedUri.authority)
        System.setProperty("spring.redis.port", parsedUri.port.toString())
    }

    runApplication<App>(*args)
}