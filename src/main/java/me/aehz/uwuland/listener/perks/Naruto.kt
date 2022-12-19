package me.aehz.uwuland.listener.global_events

import me.aehz.uwuland.Uwuland
import org.bukkit.Bukkit
import me.aehz.uwuland.managers.EventManager
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


class Naruto(
    override val plugin: Uwuland,
) : PerkListener() {
    private val lastLoc = mutableMapOf<UUID, MutableList<Location>>()

    init {
        stg["min"] = "10"
        stg["max"] = "10"
        stg["cooldown"] = "12"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onDmg(e: EntityDamageEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.entity)) return
        val owner = getOwner(e.entity.name) ?: return
        val cooldown = stg["cooldown"]!!.toInt()
        if (owner.isOnCooldown(cooldown)) return
        val loc = getTeleportLocation(e.entity.uniqueId) ?: return
        val wood = getRandomWood()
        e.entity.location.block.type = wood
        e.entity.location.block.getRelative(BlockFace.UP).type = wood
        e.entity.teleport(loc.add(0.0, 1.0, 0.0))

        owner.updateCooldown()
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