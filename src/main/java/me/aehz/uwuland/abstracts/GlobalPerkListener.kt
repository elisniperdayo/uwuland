package me.aehz.uwuland.abstracts

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.enums.ListenerType

abstract class GlobalPerkListener() : GroupPerkListener() {
    open var isGloballyEnabled = false
    override val type = ListenerType.GLOBAL_EVENT

    fun enableGlobally() {
        isGloballyEnabled = true
    }

    fun disableGlobally() {
        isGloballyEnabled = false
    }
}