package me.aehz.uwuland.listener.perks

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.interfaces.PerkListener
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.enums.ListenerType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class NarutoSwap(
    override val plugin: Uwuland,
    override var isEnabled: Boolean,
    override val type: ListenerType,
    override var perkOwners: MutableList<PerkOwner> = mutableListOf()
) :
    PerkListener {
    override var stg = mutableMapOf<String, String>()

    init {
        stg["maxDistance"] = "10"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)

    }

    @EventHandler
    fun onPlayerAttack(event: PlayerInteractEvent) {
        if (!isEnabled) return
        val maxDistance = stg["maxDistance"]!!.toInt()
        val act = event.action
        val p = event.player
        if (act == Action.LEFT_CLICK_BLOCK) {
            val block = event.player.getTargetBlock(maxDistance)
            val block2 = block!!.location.add(0.0, 1.0, 0.0).block
            val playerLocation = p.location
            val targetLocation = block!!.location.add(0.5, 0.0, 0.5)
            targetLocation.direction = p.location.direction

            val type1 = block.type
            val type2 = block2.type

            block.type = Material.AIR
            block2.type = Material.AIR

            p.teleport(targetLocation)

            playerLocation.block.type = type1
            playerLocation.add(0.0, 1.0, 0.0).block.type = type2
        }
    }
}