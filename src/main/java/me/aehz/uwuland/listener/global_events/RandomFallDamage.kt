package me.aehz.uwuland.listener.global_events

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.interfaces.GlobalPerkListener
import me.aehz.uwuland.managers.EventListenerManager
import me.aehz.uwuland.enums.ListenerType
import org.bukkit.Bukkit
import org.bukkit.entity.Enderman
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent

class RandomFallDamage(
    private val plugin: Uwuland, override var isEnabled: Boolean,
    override var isGloballyEnabled: Boolean
) : GlobalPerkListener {
    override var stg = mutableMapOf<String, String>()

    init {
        stg["min"] = "0"
        stg["max"] = "21"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventListenerManager.register(this, ListenerType.GLOBAL_EVENT)
    }

    @EventHandler
    fun onPlayerFallDamage(e: EntityDamageEvent) {
        if (!isEnabled) return
        val min = stg["min"]!!.toInt()
        val max = stg["max"]!!.toInt()
        val random = (min..max).random().toDouble()
        if (e.entity !is Enderman && e.cause == EntityDamageEvent.DamageCause.FALL) {
            e.damage = random
        }
    }
}
