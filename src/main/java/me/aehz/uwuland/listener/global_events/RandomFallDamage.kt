package me.aehz.uwuland.listener.global_events

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.abstracts.GlobalPerkListener
import me.aehz.uwuland.managers.EventManager
import org.bukkit.Bukkit
import org.bukkit.entity.Enderman
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent

class RandomFallDamage(
    override val plugin: Uwuland,
) : GlobalPerkListener() {

    init {
        stg["min"] = "0"
        stg["max"] = "21"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)
    }

    @EventHandler
    fun onPlayerFallDamage(e: EntityDamageEvent) {
        if (!isEnabled) return
        if (!isGloballyEnabled && !hasPerk(e.entity)) return
        val min = stg["min"]!!.toInt()
        val max = stg["max"]!!.toInt()
        val random = (min..max).random().toDouble()
        if (e.entity !is Enderman && e.cause == EntityDamageEvent.DamageCause.FALL) {
            e.damage = random
        }
    }
}
