package me.aehz.uwuland.interfaces

import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.enums.PerkOwnerType
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity

interface TimedPerk : PerkListener {
    fun task(targets: MutableList<LivingEntity>)
    fun startTask(owner: PerkOwner) {
        val targets = if (owner.type == PerkOwnerType.PLAYER) {
            owner.targets
        } else {
            val teamName = owner.groupAlias.substringAfter(":")
            Bukkit.getScoreboardManager().mainScoreboard.getTeam(teamName)?.entries?.mapNotNull { Bukkit.getPlayer(it) }
                ?.toMutableList<LivingEntity>() ?: mutableListOf()
        }
        val delay = (stg["min"]!!.toInt()..stg["max"]!!.toInt()).random().toLong()
        owner.taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, Runnable {
            task(targets)
            startTask(owner)
        }, delay)
    }

    fun stopTask(groupAlias: String) {
        val taskId = perkOwners.find { it.groupAlias == groupAlias }?.taskId ?: return
        Bukkit.getScheduler().cancelTask(taskId)
    }
}