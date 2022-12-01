package me.aehz.uwuland.listener.global_events

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.interfaces.GlobalPerkListener
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.enums.ListenerType
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinGreeting(
    override val plugin: Uwuland,
    override var isEnabled: Boolean,
    override var isGloballyEnabled: Boolean,
    override val type: ListenerType,
    override var perkOwners: MutableList<PerkOwner>
) : GlobalPerkListener {
    override var stg = mutableMapOf<String, String>()

    init {
        stg["message"] = "UWU"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (!isEnabled) return
        e.player.sendMessage("${stg["message"]} ${e.player.name}")
    }
}
