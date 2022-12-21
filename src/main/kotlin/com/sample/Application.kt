package com.sample

import io.ktor.server.application.*
import com.sample.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSecurity()
    configureSockets()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
