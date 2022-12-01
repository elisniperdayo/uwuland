package me.aehz.uwuland.interfaces

import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity

interface TimedPerk : PerkListener {
    fun task(targets: MutableList<LivingEntity>)
    fun startTask(targets: MutableList<LivingEntity>) {
        val delay = (stg["min"]!!.toInt()..stg["max"]!!.toInt()).random().toLong()
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, Runnable {
            task(targets)
            startTask(targets)
        }, delay)
    }

    fun stopTask(groupAlias: String) {
        val taskId = perkOwners.find { it.groupAlias == groupAlias }?.taskId ?: return
        Bukkit.getScheduler().cancelTask(taskId)
    }
}