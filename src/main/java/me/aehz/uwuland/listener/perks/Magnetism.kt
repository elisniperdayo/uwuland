package me.aehz.uwuland.listener.perks

import me.aehz.uwuland.abstracts.PerkListener
import me.aehz.uwuland.data.PerkOwner
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import java.util.UUID
import kotlin.math.max

class Magnetism() : PerkListener() {
    override var SETTING_taskDelay = 1..1
    var SETTING_range = 8.0
    var SETTING_strength = 0.5
    var SETTING_minStrength = 0.01

    val polarities = mutableMapOf<UUID, Boolean>()

    override fun setup(owner: PerkOwner): Boolean {
        polarities[owner.targets[0]] = true
        startTask(owner)
        return true
    }

    override fun unsetup(owner: PerkOwner) {
        polarities.remove(owner.targets[0])
    }

    override fun task(targets: MutableList<LivingEntity>) {
        val perkEntity = targets[0]
        val nearbyEntities = perkEntity.getNearbyEntities(SETTING_range, SETTING_range, SETTING_range)

        nearbyEntities.forEach {
            val direction = it.location.toVector().subtract(perkEntity.location.toVector()).normalize()
            val strength = calculateStrength(it.location, perkEntity.location)

            if (polarities[perkEntity.uniqueId]!!) {
                it.velocity = it.velocity.subtract(direction.multiply(strength))
            } else {
                it.velocity = it.velocity.add(direction.multiply(strength))
            }
        }
    }

    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.entity)) return
        polarities[e.entity.uniqueId] = !polarities[e.entity.uniqueId]!!
    }

    private fun calculateStrength(loc1: Location, loc2: Location): Double {
        val distance = loc1.toVector().distance(loc2.toVector())
        val maxDistance = SETTING_range * 1.5
        val strengthDistanceMultiplier = 1 - distance / maxDistance
        val calculatedStrength = SETTING_strength * strengthDistanceMultiplier / 10
        return max(calculatedStrength, SETTING_minStrength / 10)
    }
}