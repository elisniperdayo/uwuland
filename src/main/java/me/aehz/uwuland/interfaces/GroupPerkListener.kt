package me.aehz.uwuland.interfaces

import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

interface GroupPerkListener : PerkListener {
    val perkOwnerTeams: MutableList<String>

    fun addTeam(name: String) {
        val targets =
            Bukkit.getScoreboardManager().mainScoreboard.getTeam(name)?.entries?.mapNotNull { Bukkit.getPlayer(it) }
                ?.toMutableList<LivingEntity>()
                ?: return
        setup(targets)
        perkOwnerTeams.add(name)
    }

    fun removeTeam(name: String) {
        perkOwnerTeams.removeIf { it == name }
    }

    override fun hasPerk(entity: Entity): Boolean {
        val individual = super.hasPerk(entity)
        val team = Bukkit.getScoreboardManager().mainScoreboard.getEntityTeam(entity)?.name
        return (individual || perkOwnerTeams.contains(team))
    }
}