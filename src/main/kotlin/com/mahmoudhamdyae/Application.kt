package com.mahmoudhamdyae

import com.mahmoudhamdyae.database.DatabaseFactory
import com.mahmoudhamdyae.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureMonitoring()
    DatabaseFactory.init()
    configureRouting()
}
