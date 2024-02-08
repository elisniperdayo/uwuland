package me.aehz.uwuland.listener.perks

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.abstracts.PerkListener
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.managers.EventManager
import org.bukkit.Bukkit
import org.bukkit.EntityEffect
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.UUID
import kotlin.math.max
import kotlin.math.min

class Speedrunner() : PerkListener() {
    private val distance = mutableMapOf<UUID, Double>()

    override var SETTING_taskDelay = 100..100
    var SETTING_blocksPerTick = 0.25
    var SETTING_speedBoost = 23.0
    var SETTING_maxHeal = 7.0
    var SETTING_maxDamage = 9.0

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.player)) return
        val xChange = max(e.to.x, e.from.x) - min(e.to.x, e.from.x)
        val yChange = max(e.to.y, e.from.y) - min(e.to.y, e.from.y)
        val zChange = max(e.to.z, e.from.z) - min(e.to.z, e.from.z)
        distance[e.player.uniqueId] = distance[e.player.uniqueId]!! + xChange + yChange + zChange
    }

    @EventHandler
    fun onHunger(e: FoodLevelChangeEvent) {
        if (!isEnabled) return
        if (e.entity !is Player) return
        if (!hasPerk(e.entity)) return
        e.isCancelled
        e.foodLevel = 10
    }

    override fun task(targets: MutableList<LivingEntity>) {
        val player = targets[0]
        val reward = calculateReward(distance[player.uniqueId]!!)
        if (reward > 0.0) {
            player.damage(reward)
            player.playEffect(EntityEffect.HURT)
        } else if (reward < 0.0) {
            val healAmount = (reward * -1).toInt()
            player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, healAmount, 6))
        }
        distance[targets[0].uniqueId] = 0.0
    }


    private fun calculateReward(distance: Double): Double {
        val averageTime = SETTING_taskDelay.average()
        val totalSpeed = 1 + (SETTING_speedBoost / 100)
        val blocksPerTick = SETTING_blocksPerTick
        val maxDamage = SETTING_maxDamage
        val maxHeal = SETTING_maxHeal * -1

        val goal = averageTime * blocksPerTick * totalSpeed
        val rawReward = goal - distance
        // Why is there no clamp
        if (rawReward >= 1.0) {
            return min(maxDamage, rawReward * 0.8)
        } else if (rawReward <= -1.0) {
            return max(maxHeal, rawReward * 0.8)
        }
        return 0.0
    }

    override fun setup(owner: PerkOwner): Boolean {
        val player = Bukkit.getPlayer(owner.targets[0]) ?: return false
        val speedBoost = SETTING_speedBoost / 1000
        val speedMod = AttributeModifier(
            UUID.randomUUID(),
            "Speedrunner Speed Modifier",
            speedBoost,
            AttributeModifier.Operation.ADD_NUMBER
        )
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)!!.addModifier(speedMod)
        distance[player.uniqueId] = 0.0
        player.foodLevel = 10
        startTask(owner)
        return true
    }

    override fun unsetup(owner: PerkOwner) {
        val player = Bukkit.getPlayer(owner.targets[0]) ?: return
        val speedMods =
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)!!.modifiers.filter { it.name == "Speedrunner Speed Modifier" }
        speedMods.forEach { player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)!!.removeModifier(it) }
    }
}