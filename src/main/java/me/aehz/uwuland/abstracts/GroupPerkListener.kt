package me.aehz.uwuland.abstracts

import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.enums.PerkOwnerType
import org.bukkit.Bukkit

abstract class GroupPerkListener() : PerkListener() {
    override val type: ListenerType = ListenerType.GROUP_PERK

    fun addTeam(name: String) {
        if (Bukkit.getScoreboardManager().mainScoreboard.getTeam(name.removePrefix("TEAM:")) == null) return
        if (perkOwners.find { it.groupAlias == name } != null) return
        val owner = PerkOwner(PerkOwnerType.TEAM, name)
        val successfulSetup = setup(owner)
        Bukkit.getLogger().info(name)
        if (!successfulSetup) return
        perkOwners.add(owner)
    }
}