package me.aehz.uwuland.listener.global_events

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.abstracts.GlobalPerkListener
import me.aehz.uwuland.managers.EventManager
import org.bukkit.Bukkit
import org.bukkit.entity.Arrow
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

class ExplosiveArrows() : GlobalPerkListener() {
    init {
        stg["power"] = "30"
    }

    @EventHandler
    fun onEntitiyDamage(e: EntityDamageByEntityEvent) {
        if (!isEnabled) return
        if (!isGloballyEnabled && !hasPerk(e.entity)) return
        val power = stg["power"]!!.toFloat()
        if (e.damager is Arrow) {
            e.entity.location.createExplosion(power)
        }
    }
}