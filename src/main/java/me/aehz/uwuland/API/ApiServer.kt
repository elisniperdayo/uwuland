package me.aehz.uwuland.API


import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import me.aehz.uwuland.API.Routes.players
import org.bukkit.Bukkit

object KtorServer {
    fun start() {
        embeddedServer(Netty, port = 8080) {
            routing {
                players()
            }
        }.start()
        Bukkit.getLogger().info("STARTED SERVER")
    }
}