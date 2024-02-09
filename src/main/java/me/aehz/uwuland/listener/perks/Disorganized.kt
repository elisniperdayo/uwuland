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

    var totalPayouts = 0
    var common = 0
    var uncommon = 0
    var rare = 0
    var extraRare = 0
    var superExtraUltraRare = 0


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

        totalPayouts++

        if (totalPayouts % 100 == 0) Bukkit.getLogger()
            .info("Total:$totalPayouts | C:$common | U:$uncommon | R:$rare | E:$extraRare | U:$superExtraUltraRare ")

        val loot = when {
            tableRoll > 300000 -> {
                common++
                LootTables.common.random()
            }

            tableRoll > 60000 -> {
                uncommon++
                LootTables.uncommon.random()
            }

            tableRoll > 9000 -> {
                rare++
                LootTables.rare.random()
            }

            tableRoll > 1 -> {
                extraRare++
                LootTables.extraRare.random()
            }

            else -> {
                superExtraUltraRare++
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