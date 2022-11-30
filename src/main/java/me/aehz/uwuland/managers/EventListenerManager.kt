package me.aehz.uwuland.managers

import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.interfaces.PerkListener
import org.bukkit.entity.LivingEntity

object EventListenerManager {
    val listeners = mutableMapOf<String, PerkListener>()
    val perks = mutableMapOf<String, MutableList<LivingEntity>>()
    val groupPerks = mutableMapOf<String, MutableList<MutableList<LivingEntity>>>()
    val globalPerks = mutableMapOf<String, MutableList<MutableList<LivingEntity>>>()

    fun register(
        listener: PerkListener,
        type: ListenerType,
        alias: String = listener.javaClass.name.substringAfterLast(".")
    ) {
        listeners[alias] = listener
        when (type) {
            ListenerType.PERK -> perks[alias] = mutableListOf<LivingEntity>()
            ListenerType.GROUP_PERK -> groupPerks[alias] = mutableListOf<MutableList<LivingEntity>>()
            ListenerType.GLOBAL_EVENT -> globalPerks[alias] = mutableListOf<MutableList<LivingEntity>>()
        }
    }

    fun get(alias: String): PerkListener {
        return listeners[alias]!!
    }
}




