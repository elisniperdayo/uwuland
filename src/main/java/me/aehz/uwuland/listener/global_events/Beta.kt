package me.aehz.uwuland.listener.global_events


import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.interfaces.GlobalPerkListener
import org.bukkit.Bukkit
import me.aehz.uwuland.managers.EventListenerManager
import me.aehz.uwuland.enums.ListenerType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent


class Beta(private val plugin: Uwuland, override var isEnabled: Boolean, override var isGloballyEnabled: Boolean) :
    GlobalPerkListener {
    override var stg = mutableMapOf<String, String>()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventListenerManager.register(this, ListenerType.GLOBAL_EVENT)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onDamage(e: EntityDamageEvent) {
        if (!isEnabled) return
        val p = e.entity
        if (p !is Player) return

        p.damage(20.0)
        p.health += 5.0
    }
}