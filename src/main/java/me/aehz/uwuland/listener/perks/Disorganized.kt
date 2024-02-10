package me.aehz.uwuland.listener.perks

import me.aehz.uwuland.abstracts.PerkListener
import me.aehz.uwuland.data.LootTables
import me.aehz.uwuland.data.PerkOwner
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack


class Disorganized() : PerkListener() {

    override var SETTING_taskDelay = 20..60
    var SETTING_payoutChance = 4
    val nonHotbarRange = 9..35

    @EventHandler
    fun onDeath(e: PlayerDeathEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.entity)) return
        val itemsToKeep = e.player.inventory.contents.filterIndexed { i, _ -> i !in nonHotbarRange }
        itemsToKeep.forEach {
            e.drops.remove(it)
            e.itemsToKeep.add(it)
        }
    }

    override fun setup(owner: PerkOwner): Boolean {
        startTask(owner)
        return true
    }

    override fun task(targets: MutableList<LivingEntity>) {
        val player = targets[0]
        if (player !is Player) return

        handlePayout(player)

        val originalContents = player.inventory.contents
        val shuffledContents = player.inventory.contents.filterIndexed { i, _ -> i in nonHotbarRange }.shuffled()
        val combinedContents = originalContents.mapIndexed { i, itemStack ->
            if (i in nonHotbarRange) {
                shuffledContents[i - nonHotbarRange.first()]
            } else {
                itemStack
            }
        }

        player.inventory.contents = combinedContents.toTypedArray()
    }

    private fun calculatePayout(): ItemStack? {
        if ((1..100).random() > SETTING_payoutChance) return null
        val tableRoll = (1..1000000).random()

        val loot = when {
            tableRoll > 300000 -> {
                LootTables.common.random()
            }

            tableRoll > 60000 -> {
                LootTables.uncommon.random()
            }

            tableRoll > 9000 -> {
                LootTables.rare.random()
            }

            tableRoll > 1 -> {
                LootTables.extraRare.random()
            }

            else -> {
                return LootTables.ultraSuperExtraRare.random()
            }
        }
        return ItemStack(loot.material, loot.amount.random())
    }

    private fun handlePayout(player: Player) {
        val payout = calculatePayout()
        if (payout != null) {
            val slot = player.inventory.firstEmpty()
            if (slot == -1) player.world.dropItemNaturally(player.location, payout)
            else player.inventory.setItem(slot, payout)
        }
    }
}