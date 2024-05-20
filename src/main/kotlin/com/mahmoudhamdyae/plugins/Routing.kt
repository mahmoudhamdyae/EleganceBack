package com.mahmoudhamdyae.plugins

import com.mahmoudhamdyae.services.tasks.routes.tasksRoute
import com.mahmoudhamdyae.services.users.routes.userRoute
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        userRoute()
        tasksRoute()
    }
}
