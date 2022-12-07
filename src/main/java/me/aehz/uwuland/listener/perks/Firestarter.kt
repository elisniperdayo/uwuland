package me.aehz.uwuland.listener.perks

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.interfaces.PerkListener
import me.aehz.uwuland.interfaces.TimedPerk
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.util.getAdjacent
import me.aehz.uwuland.util.getRadius
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent


class Firestarter(
    override val plugin: Uwuland,
    override var isEnabled: Boolean,
    override val type: ListenerType,
    override var perkOwners: MutableList<PerkOwner> = mutableListOf()
) : PerkListener, TimedPerk {
    override var stg = mutableMapOf<String, String>()

    init {
        stg["min"] = "420"
        stg["max"] = "720"
        stg["amount"] = "4"
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
        val burnableBlocks = getRadius(perkEntity.location, range).filter { it.isBurnable }

        if (burnableBlocks.isEmpty()) return

        Bukkit.getLogger().info("BURNING")
        val burnableAir =
            burnableBlocks.mapNotNull { block ->
                getAdjacent(block).filter { it.type == Material.AIR && it.getRelative(BlockFace.DOWN).isSolid }
                    .randomOrNull()
            }.toMutableList()

        var i = 0
        while (i < stg["amount"]!!.toInt() && burnableAir.isNotEmpty()) {
            val block = burnableAir.random()
            block.type = Material.FIRE
            burnableAir.removeIf { it == block }
            i += 1
        }
    }


    @EventHandler
    fun onDmg(e: EntityDamageEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.entity)) return
        if (e.cause == EntityDamageEvent.DamageCause.FIRE || e.cause == EntityDamageEvent.DamageCause.FIRE_TICK || e.cause == EntityDamageEvent.DamageCause.LAVA) {
            e.isCancelled = true
        }
    }
}