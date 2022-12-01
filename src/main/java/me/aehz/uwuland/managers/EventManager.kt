package me.aehz.uwuland.managers

import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.interfaces.PerkListener
import org.bukkit.entity.Entity

object EventManager {
    val listeners = mutableMapOf<String, PerkListener>()

    fun register(
        listener: PerkListener,
        type: ListenerType,
        alias: String = listener.javaClass.name.substringAfterLast(".")
    ) {
        listeners[alias] = listener
    }

    fun get(alias: String): PerkListener {
        return listeners[alias]!!
    }

    fun getPerksByEntity(entity: Entity): Set<String> {
        val perks = mutableSetOf<String>()
        listeners.forEach {
            if (it.value.hasPerk(entity)) {
                perks.add(it.key)
            }
        }
        return perks
    }
}