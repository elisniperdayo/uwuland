package me.aehz.uwuland.listener.perks

import io.papermc.paper.event.player.PlayerArmSwingEvent
import me.aehz.uwuland.abstracts.*
import me.aehz.uwuland.util.swapEntities
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler

class Abductor() : PerkListener() {

    override var SETTING_cooldown = 5
    var SETTING_maxRange = 42
    var SETTING_minRange = 4
    var SETTING_swapInventory = false


    @EventHandler
    fun onArmSwing(e: PlayerArmSwingEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.player)) return
        val owner = getOwner(e.player.name) ?: return
        val p = e.player
        val targetMax = p.getTargetEntity(SETTING_maxRange)
        val targetMin = p.getTargetEntity(SETTING_minRange)
        if (targetMax !is LivingEntity) return
        if (targetMax == targetMin) return
        if (!handleCooldown(e.player)) return
        swapEntities(p, targetMax, SETTING_swapInventory)
    }
}