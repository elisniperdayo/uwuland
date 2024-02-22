package me.aehz.uwuland.API.Routes

import io.ktor.server.application.*

import io.ktor.server.routing.*
import me.aehz.uwuland.API.Controllers.PlayerController

fun Route.players() {
    get("players/sse") {
        PlayerController.get(call)
    }
}