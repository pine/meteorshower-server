package moe.pine.meteorshower

import moe.pine.heroku.addons.HerokuRedis
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

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

    if (HerokuRedis.isDetected()) {
        System.setProperty("spring.redis.host", HerokuRedis.getHost())
        System.setProperty("spring.redis.password", HerokuRedis.getPassword())
        System.setProperty("spring.redis.port", Integer.toString(HerokuRedis.getPort()))
    }

    runApplication<App>(*args)
}