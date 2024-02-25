package me.aehz.uwuland.API.Routes

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.routing.*
import me.aehz.uwuland.API.Controllers.TeamController
import org.bukkit.Bukkit

fun Route.teams() {

    get("teams/sse") {
        TeamController.sse(call)
    }

    post("teams/{teamName}") {
        TeamController.post(call)
    }

    put("teams/{teamName}") {
        TeamController.put(call)
    }

    delete("teams/{teamName}") {
        TeamController.delete(call)
    }

    get("teams/colors") {
        call.caching = CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 86400, mustRevalidate = false))
        TeamController.getColor(call)
    }
}