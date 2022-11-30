package me.aehz.uwuland.listener

import me.aehz.uwuland.Uwuland
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class ProtectDBBlock(private val plugin: Uwuland) : Listener {
    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onBreak(e: BlockBreakEvent) {
        val DBWorld = Bukkit.getWorld("DatabaseWorld847593474398")
        if (e.block.world != DBWorld) return
        val DBblock = DBWorld.getBlockAt(0, 123, 0)
        if (e.block == DBblock) e.isCancelled = true
    }
}