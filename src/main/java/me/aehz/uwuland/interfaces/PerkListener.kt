package me.aehz.uwuland.interfaces

import org.bukkit.event.Listener

interface PerkListener : Listener {
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