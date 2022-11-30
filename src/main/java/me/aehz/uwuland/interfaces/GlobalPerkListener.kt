package me.aehz.uwuland.interfaces

interface GlobalPerkListener : PerkListener {
    var isGloballyEnabled: Boolean

    fun enableGlobally() {
        isGloballyEnabled = true
    }

    fun disableGlobally() {
        isGloballyEnabled = false
    }
}