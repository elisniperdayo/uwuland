package me.aehz.uwuland.util

import org.bukkit.event.Listener

object EventListenerManager {
    val listeners = mutableMapOf<String, CustomListener>()

    fun register(listener: CustomListener, alias: String = listener.javaClass.name.substringAfterLast(".")) {
        listeners[alias] = listener
    }

    fun get(alias: String): CustomListener {
        return listeners[alias]!!
    }
}

interface CustomListener : Listener {
    var isEnabled: Boolean
    var stg: MutableMap<String, String>

    fun enable() {
        isEnabled = true
    }

    fun disable() {
        isEnabled = false
    }

    fun setStg(key: String, value: String): Boolean {
        if (!this.stg.containsKey(key)) return false
        this.stg[key] = value
        return true
    }
}