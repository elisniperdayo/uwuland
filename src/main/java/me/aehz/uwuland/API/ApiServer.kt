package me.aehz.uwuland.API


import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import me.aehz.uwuland.API.Routes.events
import me.aehz.uwuland.API.Routes.perks
import me.aehz.uwuland.API.Routes.players
import me.aehz.uwuland.API.Routes.teams
import org.bukkit.Bukkit

object KtorServer {
    fun start() {
        embeddedServer(Netty, port = 8080) {
            install(ContentNegotiation) {
                gson()
            }
            install(CORS) {
                allowHost("localhost:3000")
                allowMethod(HttpMethod.Delete)
            }

            routing {
                players()
                teams()
                perks()
                events()
            }
        }.start()
        Bukkit.getLogger().info("STARTED SERVER")
    }
}
