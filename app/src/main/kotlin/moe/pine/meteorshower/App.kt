package moe.pine.meteorshower

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.net.URI

@SpringBootApplication
class App {
    companion object {
        @JvmField
        val LOGGER: Logger = LoggerFactory.getLogger(App::class.java)
    }
}

fun main(args: Array<String>) {
    val port = System.getenv("PORT") ?: ""
    if (port.isNotEmpty()) {
        System.setProperty("server.port", port)
    }

    val redisUrl = System.getenv("REDIS_URL") ?: ""
    if (redisUrl.isNotEmpty()) {
        val parsedUri = URI.create(redisUrl)
        val password = parsedUri.userInfo.split(":")[1]
        App.LOGGER.debug("spring.redis.host={}", parsedUri.host)
        App.LOGGER.debug("spring.redis.password={}", password)
        App.LOGGER.debug("spring.redis.port={}", parsedUri.port)
        System.setProperty("spring.redis.host", parsedUri.host)
        System.setProperty("spring.redis.password", password)
        System.setProperty("spring.redis.port", parsedUri.port.toString())
    }

    runApplication<App>(*args)
}