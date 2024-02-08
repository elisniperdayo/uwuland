package me.aehz.uwuland.listener.group_perks

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.abstracts.GroupPerkListener
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.util.swapEntities
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity


class Shuffle() : GroupPerkListener() {

    override var SETTING_taskDelay = 13000..23000

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
            e1.sendMessage("You have been swapped with ${e2.name}")
            e2.sendMessage("You have been swapped with ${e1.name} ")
            swapEntities(e1, e2)
        }
    }
}
