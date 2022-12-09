package me.aehz.uwuland.listener.perks

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.interfaces.PerkListener
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.util.BlockUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.event.player.PlayerMoveEvent


class Xray(
    override val plugin: Uwuland,
    override var isEnabled: Boolean,
    override val type: ListenerType,
    override var perkOwners: MutableList<PerkOwner> = mutableListOf()
) : PerkListener {
    override var stg = mutableMapOf<String, String>()
    private val barrierData = Bukkit.createBlockData(Material.BARRIER)
    private val positions = mutableMapOf<String, Block>()

    init {
        stg["range"] = "12.0"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)
    }

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.player)) return
        if (positions[e.player.name] == e.player.location.block) return
        val range = stg["range"]!!.toDouble()
        val r = BlockUtil.getRadiusSolid(
            e.player.location.block,
            range
        ).filter { it.isSolid }
        r.forEach {
            if (it.location.x == e.player.location.block.x + range
                || it.location.x == e.player.location.block.x - range
                || it.location.y == e.player.location.block.y + range
                || it.location.y == e.player.location.block.y - range
                || it.location.z == e.player.location.block.z + range
                || it.location.z == e.player.location.block.z - range
            ) {
                e.player.sendBlockChange(it.location, Bukkit.createBlockData(it.type))
            } else {
                e.player.sendBlockChange(it.location, barrierData)
            }
        }

        positions[e.player.name] = e.player.location.block
    }

    @EventHandler
    fun onMine(e: BlockDamageEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.player)) return
        e.player.sendBlockChange(e.block.location, Bukkit.createBlockData(e.block.type))
    }
}