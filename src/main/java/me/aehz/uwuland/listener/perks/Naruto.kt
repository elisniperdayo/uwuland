package me.aehz.uwuland.listener.global_events

import org.bukkit.Bukkit
import me.aehz.uwuland.abstracts.PerkListener
import me.aehz.uwuland.data.PerkOwner
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import java.util.UUID


class Naruto() : PerkListener() {
    private val lastLoc = mutableMapOf<UUID, MutableList<Location>>()

    override var SETTING_taskDelay = 10..10
    override var SETTING_cooldown = 12

    @EventHandler(priority = EventPriority.LOWEST)
    fun onDmg(e: EntityDamageEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.entity)) return
        val loc = getTeleportLocation(e.entity.uniqueId) ?: return
        if (!handleCooldown(e.entity)) return
        val wood = getRandomWood()
        e.entity.location.block.type = wood
        e.entity.location.block.getRelative(BlockFace.UP).type = wood
        e.entity.teleport(loc.add(0.0, 1.0, 0.0))
        e.damage = 0.0
    }

    override fun task(targets: MutableList<LivingEntity>) {
        val p = targets[0]
        if (lastLoc[p.uniqueId]!!.size == 100) lastLoc[p.uniqueId]!!.removeAt(99)
        lastLoc[p.uniqueId]!!.add(0, p.location)
    }

    override fun setup(owner: PerkOwner): Boolean {
        val p = Bukkit.getPlayer(owner.targets[0]) ?: return false
        lastLoc[p.uniqueId] = mutableListOf(p.location)
        startTask(owner)
        return true
    }

    private fun getTeleportLocation(id: UUID): Location? {
        if (lastLoc[id]!!.size < 10) return null
        lastLoc[id]!!.subList(0, 10).clear()
        for (loc in lastLoc[id]!!) {
            val block = loc.block
            val blockUp = block.getRelative(BlockFace.UP)
            if (block.isSolid || blockUp.isSolid) continue

            val blockDown1 = block.getRelative(BlockFace.DOWN)
            val blockDown2 = blockDown1.getRelative(BlockFace.DOWN)
            if (blockDown1.isSolid || blockDown2.isSolid
            ) {
                return loc
            }
        }
        Bukkit.getLogger().info("NO BLOCK FOUND")
        return null
    }

    private fun getRandomWood(): Material {
        val arr = arrayOf<Material>(
            Material.ACACIA_WOOD,
            Material.BIRCH_WOOD,
            Material.DARK_OAK_WOOD,
            Material.JUNGLE_WOOD,
            Material.MANGROVE_WOOD,
            Material.OAK_WOOD,
            Material.SPRUCE_WOOD
        )
        return arr.random()
    }
}