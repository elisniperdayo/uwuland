package me.aehz.uwuland.listener.group_perks

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.abstracts.GroupPerkListener
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.util.swapEntities
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity


class Shuffle(
    override val plugin: Uwuland,
) : GroupPerkListener() {

    init {
        stg["min"] = "13000"
        stg["max"] = "23000"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)
    }

    override fun setup(owner: PerkOwner): Boolean {
        startTask(owner)
        return true
    }

    override fun task(targets: MutableList<LivingEntity>) {
        shuffleList(targets)
    }

    private fun shuffleList(targets: MutableList<LivingEntity>) {
        var i = targets.size
        while (i > 1) {
            i--
            val random = (0..i - 1).random()
            val e1 = targets[i]
            val e2 = targets[random]
            swapEntities(e1, e2)
        }
    }
}
