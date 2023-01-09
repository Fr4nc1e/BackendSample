package com.sample

import com.sample.di.mainModule
import com.sample.plugins.*
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(Koin) {
        slf4jLogger()
        modules(mainModule)
    }
    configureSecurity()
    configureSockets()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
