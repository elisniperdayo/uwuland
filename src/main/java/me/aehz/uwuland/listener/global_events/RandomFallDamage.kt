package me.aehz.uwuland.listener.global_events

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.abstracts.GlobalPerkListener
import me.aehz.uwuland.managers.EventManager
import org.bukkit.Bukkit
import org.bukkit.entity.Enderman
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent

class RandomFallDamage() : GlobalPerkListener() {

    var SETTING_minDamage = 0
    var SETTING_maxDamage = 21

    @EventHandler
    fun onPlayerFallDamage(e: EntityDamageEvent) {
        if (!isEnabled) return
        if (!isGloballyEnabled && !hasPerk(e.entity)) return
        val min = SETTING_minDamage
        val max = SETTING_maxDamage
        val random = (min..max).random().toDouble()
        if (e.entity !is Enderman && e.cause == EntityDamageEvent.DamageCause.FALL) {
            e.damage = random
        }
    }
}
