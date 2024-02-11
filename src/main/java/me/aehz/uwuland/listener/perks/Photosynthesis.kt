package me.aehz.uwuland.listener.perks

import me.aehz.uwuland.abstracts.PerkListener
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.util.EntityUtil
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.min

class Photosynthesis() : PerkListener() {

    override var SETTING_taskDelay = 100..100
    var SETTING_minLightLevel = 15
    var SETTING_buffChance = 15
    var SETTING_buffDuration = 1200

    private val nightTimeRange = 13200..23000

    private val buffs = arrayOf<PotionEffect>(
        PotionEffect(PotionEffectType.SPEED, 0, 2, false, false, false),
        PotionEffect(PotionEffectType.FAST_DIGGING, 0, 3, false, false, false),
        PotionEffect(PotionEffectType.LUCK, 0, 2, false, false, false),
        PotionEffect(PotionEffectType.DOLPHINS_GRACE, 0, 1, false, false, false),
        PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, 1, false, false, false),
        PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 1, false, false, false),
        PotionEffect(PotionEffectType.ABSORPTION, 0, 1, false, false, false),
    )


    override fun setup(owner: PerkOwner): Boolean {
        startTask(owner)
        return true
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onFoodLevelChange(e: FoodLevelChangeEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.entity)) return
        if (EntityUtil.isInNether(e.entity) || EntityUtil.isInEnd(e.entity)) return

        e.isCancelled = true
        val currentFoodLevel = e.entity.foodLevel
        val newFoodLevel = e.foodLevel
        Bukkit.getLogger().info("$newFoodLevel")
        if (newFoodLevel > currentFoodLevel) return
        if (canPhotosynthesize(e.entity as Player)) return
        e.entity.foodLevel = e.foodLevel
    }

    override fun task(targets: MutableList<LivingEntity>) {
        if (targets[0] !is Player) return
        if (targets[0].location.block.lightFromSky < 10) return
        val target = targets[0] as Player
        if (!canPhotosynthesize(target)) return
        target.foodLevel = min(target.foodLevel + 2, 20)

        if ((1..100).random() <= SETTING_buffChance) {
            buffs.random().withDuration(SETTING_buffDuration).apply(target)
        }
    }

    private fun canPhotosynthesize(player: Player): Boolean {
        val isDayTime = player.world.time !in nightTimeRange
        val isBrightEnough = (player.location.block.lightFromSky..15).random() >= SETTING_minLightLevel
        return isDayTime && isBrightEnough
    }
}