package moe.pine.meteorshower

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class App

fun main(args: Array<String>) {
    val port = System.getenv("PORT") ?: ""
    if (port.isNotEmpty()) {
        System.setProperty("server.port", port)
    }

    runApplication<App>(*args)
}