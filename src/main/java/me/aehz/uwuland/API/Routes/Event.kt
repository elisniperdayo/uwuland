package me.aehz.uwuland.API.Routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import me.aehz.uwuland.API.Controllers.EventController
import org.bukkit.Bukkit

fun Route.events() {
    get("events/sse") {
        Bukkit.getLogger().info("SSE ROUTE CALLED")
        EventController.sse(call)
    }
}