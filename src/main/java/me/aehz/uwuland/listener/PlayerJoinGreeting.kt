package me.aehz.uwuland.listener

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.util.CustomListener
import me.aehz.uwuland.util.EventListenerManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinGreeting(private val plugin: Uwuland, override var isEnabled: Boolean) : CustomListener {
    override var stg = mutableMapOf<String, String>()

    init {
        stg["message"] = "UWU"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventListenerManager.register(this)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (!isEnabled) return
        e.player.sendMessage("${stg["message"]} ${e.player.name}")
    }
}

