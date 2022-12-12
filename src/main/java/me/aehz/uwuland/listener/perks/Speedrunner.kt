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
import java.util.UUID
import kotlin.math.max
import kotlin.math.min

class Speedrunner(
    override val plugin: Uwuland,
) : PerkListener() {
    val distance = mutableMapOf<UUID, Double>()

    init {
        stg["min"] = "100"
        stg["max"] = "100"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)
    }

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.player)) return
        val xChange = max(e.to.x, e.from.x) - min(e.to.x, e.from.x)
        var yChange = max(e.to.y, e.from.y) - min(e.to.y, e.from.y)
        val zChange = max(e.to.z, e.from.z) - min(e.to.z, e.from.z)
        // Increase value of Ychange if player is primarily moving upwards
        if (xChange + zChange < yChange / 2) {
            yChange *= 2.4
        }
        distance[e.player.uniqueId] = distance[e.player.uniqueId]!! + xChange + yChange + zChange
    }

    @EventHandler
    fun onHunger(e: FoodLevelChangeEvent) {
        if (!isEnabled) return
        if (e.entity !is Player) return
        if (!hasPerk(e.entity)) return
        Bukkit.getLogger().info("FOOD EVENT")
        Bukkit.getLogger().info("FOOD EVENT")
        e.isCancelled
        e.foodLevel = 10
    }

    override fun task(targets: MutableList<LivingEntity>) {
        val player = targets[0]
        Bukkit.getLogger().info("MOVED DISTANCE OF ${distance[targets[0].uniqueId]}")
        var amount = (40 - distance[player.uniqueId]!!) / 3.5
        if (amount > 0.5) {
            player.damage(amount)
            player.playEffect(EntityEffect.HURT)
        } else if (amount < -0.5) {
            val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
            amount = (amount * -1) / 1.5
            player.health = min(player.health + amount, maxHealth)
        }
        distance[targets[0].uniqueId] = 0.0
    }

    override fun setup(owner: PerkOwner): Boolean {
        val player = Bukkit.getPlayer(owner.targets[0]) ?: return false
        val speedMod = AttributeModifier(
            UUID.randomUUID(),
            "Speedrunner Speed Modifier",
            0.023,
            AttributeModifier.Operation.ADD_NUMBER
        )
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)!!.addModifier(speedMod)
        distance[player.uniqueId] = 0.0
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