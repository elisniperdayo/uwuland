package me.aehz.uwuland.listener

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.util.CustomListener
import me.aehz.uwuland.util.EventListenerManager
import org.bukkit.Bukkit
import org.bukkit.entity.Arrow
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

class ExplosiveArrows(private val plugin: Uwuland, override var isEnabled: Boolean) : CustomListener {
    override var stg = mutableMapOf<String, String>()

    init {
        stg["power"] = "30"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventListenerManager.register(this)
    }

    @EventHandler
    fun onEntitiyDamage(event: EntityDamageByEntityEvent) {
        if (!isEnabled) return
        val power = stg["power"]!!.toFloat()
        if (event.damager is Arrow) {
            event.entity.location.createExplosion(power)
        }
    }
}