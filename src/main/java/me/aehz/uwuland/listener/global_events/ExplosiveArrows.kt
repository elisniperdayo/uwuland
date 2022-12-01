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
    private val plugin: Uwuland, override var isEnabled: Boolean,
    override var isGloballyEnabled: Boolean,
    override val type: ListenerType,
    override var perkOwners: MutableList<PerkOwner>
) : GlobalPerkListener {
    override var stg = mutableMapOf<String, String>()

    init {
        stg["power"] = "30"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)
    }

    @EventHandler
    fun onEntitiyDamage(event: EntityDamageByEntityEvent) {
        if (!isEnabled) return
        val power = stg["power"]!!.toFloat()
        if (event.damager is Arrow) {
            event.entity.location.createExplosion(power)
        }
    }
}