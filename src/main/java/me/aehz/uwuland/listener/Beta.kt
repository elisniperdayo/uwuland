package me.aehz.uwuland.listener


import me.aehz.uwuland.Uwuland
import org.bukkit.Bukkit
import me.aehz.uwuland.util.CustomListener
import me.aehz.uwuland.util.EventListenerManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent


class Beta(private val plugin: Uwuland, override var isEnabled: Boolean) : CustomListener {
    override var stg = mutableMapOf<String, String>()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventListenerManager.register(this)
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