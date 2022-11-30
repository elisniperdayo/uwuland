package me.aehz.uwuland.listener

import io.papermc.paper.event.player.PlayerArmSwingEvent
import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.util.CustomListener
import me.aehz.uwuland.util.EventListenerManager
import me.aehz.uwuland.util.swapEntities
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler

class SwapStick(private val plugin: Uwuland, override var isEnabled: Boolean) : CustomListener {
    override var stg = mutableMapOf<String, String>()

    init {
        stg["material"] = "STICK"
        stg["maxDistance"] = "100"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventListenerManager.register(this)
    }

    @EventHandler
    fun onStickClick(e: PlayerArmSwingEvent) {
        if (!isEnabled) return
        val material = Material.matchMaterial(stg["material"]!!)
        val maxDistance = stg["maxDistance"]!!.toInt()
        val p = e.player
        val target = e.player.getTargetEntity(maxDistance)
        if (p.inventory.itemInMainHand.type != material || target !is Entity) return
        swapEntities(p, target)
    }
}