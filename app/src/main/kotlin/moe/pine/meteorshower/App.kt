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
        println(parsedUri)
    }

    runApplication<App>(*args)
}