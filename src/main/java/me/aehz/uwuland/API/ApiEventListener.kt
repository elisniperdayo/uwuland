package me.aehz.uwuland.API

import me.aehz.uwuland.API.Data.ApiEvent
import me.aehz.uwuland.API.Data.ApiEventData
import me.aehz.uwuland.PluginInstance
import me.aehz.uwuland.enums.ApiEventType
import me.aehz.uwuland.managers.ApiEventManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.entity.Tameable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent

class ApiEventListener : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, PluginInstance.get()!!)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamage(e: EntityDamageEvent) {
        val isTamed = e.entity is Tameable && (e.entity as Tameable).isTamed
        Bukkit.getLogger().info("$isTamed")
        if (e.entity !is Player && !isTamed) return
        val data = ApiEventData.Damage(
            e.entity.location.toVector(),
            e.entity.uniqueId,
            e.finalDamage,
            e.cause.name
        )
        val event = ApiEvent(ApiEventType.DAMAGE, data)
        ApiEventManager.add(event)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        Bukkit.getLogger().info("JOINED")
        Bukkit.getLogger().info("JOINED")
        Bukkit.getLogger().info("JOINED")
    }
}