package me.aehz.uwuland.interfaces

import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.enums.PerkOwnerType

interface GroupPerkListener : PerkListener {

    fun addTeam(name: String) {
        if (perkOwners.find { it.groupAlias == name } != null) return
        val owner = PerkOwner(PerkOwnerType.TEAM, name)
        val successfulSetup = setup(owner)
        if (!successfulSetup) return
        perkOwners.add(owner)
    }
}