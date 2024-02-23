package me.aehz.uwuland.API.Routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import me.aehz.uwuland.API.Controllers.EventController

fun Route.events() {
    get("events/sse") {
        EventController.sse(call)
    }
}