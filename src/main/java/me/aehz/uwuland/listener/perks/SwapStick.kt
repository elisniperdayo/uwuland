package me.aehz.uwuland.listener.perks

import io.papermc.paper.event.player.PlayerArmSwingEvent
import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.abstracts.PerkListener
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.util.swapEntities
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler

class SwapStick() : PerkListener() {

    init {
        stg["material"] = "STICK"
        stg["maxDistance"] = "100"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)
    }

    @EventHandler
    fun onStickClick(e: PlayerArmSwingEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.player)) return
        val material = Material.matchMaterial(stg["material"]!!)
        val maxDistance = stg["maxDistance"]!!.toInt()
        val p = e.player
        val target = e.player.getTargetEntity(maxDistance)
        if (p.inventory.itemInMainHand.type != material || target !is Entity) return
        swapEntities(p, target)
    }
}