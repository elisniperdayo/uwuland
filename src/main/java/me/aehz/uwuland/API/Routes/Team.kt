package me.aehz.uwuland.API.Routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import me.aehz.uwuland.API.Controllers.TeamController

fun Route.teams() {
    get("teams") {
        TeamController.get(call)
    }
}