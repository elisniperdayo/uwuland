package me.aehz.uwuland.interfaces

interface GlobalPerkListener : GroupPerkListener {
    var isGloballyEnabled: Boolean

    fun enableGlobally() {
        isGloballyEnabled = true
    }

    fun disableGlobally() {
        isGloballyEnabled = false
    }
}