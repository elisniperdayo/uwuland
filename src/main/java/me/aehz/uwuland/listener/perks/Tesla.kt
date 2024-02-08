package me.aehz.uwuland.listener.perks

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.abstracts.PerkListener
import me.aehz.uwuland.managers.EventManager
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent


class Tesla() : PerkListener() {

    override var SETTING_taskDelay = 40..140
    var SETTING_amount = 5
    var SETTING_range = 12.0

    override fun setup(owner: PerkOwner): Boolean {
        startTask(owner)
        return true
    }

    override fun task(targets: MutableList<LivingEntity>) {
        val perkEntity = targets[0]
        val nearbyEntities = perkEntity.getNearbyEntities(SETTING_range, 20.0, SETTING_range)

        var i = 0
        while (i < SETTING_amount && nearbyEntities.isNotEmpty()) {
            val en = nearbyEntities.random()
            en.location.world.strikeLightning(en.location)
            nearbyEntities.removeIf { it == en }
            i++
        }
    }

    @EventHandler
    fun onDmg(e: EntityDamageEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.entity)) return
        if (e.cause == EntityDamageEvent.DamageCause.LIGHTNING) {
            e.isCancelled = true
        }
    }
}