package me.aehz.uwuland.listener.global_events

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.abstracts.GlobalPerkListener
import me.aehz.uwuland.managers.EventManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinGreeting(
    override val plugin: Uwuland,
) : GlobalPerkListener() {

    init {
        stg["message"] = "UWU"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (!isEnabled) return
        if (!isGloballyEnabled && !hasPerk(e.player)) return
        e.player.sendMessage("${stg["message"]} ${e.player.name}")
    }
}
