package me.aehz.uwuland.listener.perks

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.abstracts.PerkListener
import me.aehz.uwuland.managers.EventManager
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent


class Tesla(
    override val plugin: Uwuland,
) : PerkListener() {

    init {
        stg["min"] = "40"
        stg["max"] = "140"
        stg["amount"] = "5"
        stg["range"] = "12"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)
    }

    override fun setup(owner: PerkOwner): Boolean {
        startTask(owner)
        return true
    }

    override fun task(targets: MutableList<LivingEntity>) {
        val perkEntity = targets[0]
        val range = stg["range"]!!.toDouble()
        val nearbyEntities = perkEntity.getNearbyEntities(range, 20.0, range)

        var i = 0
        while (i < stg["amount"]!!.toInt() && nearbyEntities.isNotEmpty()) {
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