package me.aehz.uwuland.abstracts

import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.enums.PerkOwnerType
import me.aehz.uwuland.Uwuland
import org.bukkit.Bukkit

abstract class GroupPerkListener() : PerkListener() {
    override val type: ListenerType = ListenerType.GROUP_PERK

    fun addTeam(name: String) {
        if (perkOwners.find { it.groupAlias == name } != null) return
        val owner = PerkOwner(PerkOwnerType.TEAM, name)
        val successfulSetup = setup(owner)
        Bukkit.getLogger().info(name)
        //TODO MAKE IT CALL SETUP ON TEAM MEMBERS
        // SHOULDNT SETUP CALL THE SAMME FUNCTION AS startTask
        if (!successfulSetup) return
        perkOwners.add(owner)
    }
}