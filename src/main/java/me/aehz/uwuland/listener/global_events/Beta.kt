package me.aehz.uwuland.listener.global_events


import io.papermc.paper.event.player.PlayerArmSwingEvent
import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.interfaces.GlobalPerkListener
import org.bukkit.Bukkit
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.enums.ListenerType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent


class Beta(
    private val plugin: Uwuland,
    override var isEnabled: Boolean,
    override var isGloballyEnabled: Boolean,
    override val type: ListenerType,
    override var perkOwners: MutableList<PerkOwner>
) :
    GlobalPerkListener {
    override var stg = mutableMapOf<String, String>()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onDamage(e: PlayerArmSwingEvent) {
        if (!isEnabled) return
        Bukkit.getLogger().info("${EventManager.getPerksByEntity(e.player)}")
    }
}