package me.aehz.uwuland.listener.global_events

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.interfaces.GlobalPerkListener
import me.aehz.uwuland.managers.EventListenerManager
import me.aehz.uwuland.enums.ListenerType
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinGreeting(
    private val plugin: Uwuland, override var isEnabled: Boolean,
    override var isGloballyEnabled: Boolean
) : GlobalPerkListener {
    override var stg = mutableMapOf<String, String>()

    init {
        stg["message"] = "UWU"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventListenerManager.register(this, ListenerType.GLOBAL_EVENT)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (!isEnabled) return
        e.player.sendMessage("${stg["message"]} ${e.player.name}")
    }
}
