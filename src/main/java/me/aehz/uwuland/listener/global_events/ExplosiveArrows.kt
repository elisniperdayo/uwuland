package me.aehz.uwuland.listener.global_events

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.interfaces.GlobalPerkListener
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.enums.ListenerType
import org.bukkit.Bukkit
import org.bukkit.entity.Arrow
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

class ExplosiveArrows(
    override val plugin: Uwuland,
    override var isEnabled: Boolean,
    override var isGloballyEnabled: Boolean,
    override val type: ListenerType,
    override var perkOwners: MutableList<PerkOwner> = mutableListOf(),
) : GlobalPerkListener {
    override var stg = mutableMapOf<String, String>()

    init {
        stg["power"] = "30"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)
    }

    @EventHandler
    fun onEntitiyDamage(e: EntityDamageByEntityEvent) {
        if (!isEnabled) return
        if (!isGloballyEnabled && !hasPerk(e.entity)) return
        val power = stg["power"]!!.toFloat()
        if (e.damager is Arrow) {
            e.entity.location.createExplosion(power)
        }
    }
}