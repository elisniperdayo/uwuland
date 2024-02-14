package me.aehz.uwuland.listener.group_perks

import com.destroystokyo.paper.event.entity.EntityJumpEvent
import io.papermc.paper.event.player.AsyncChatEvent
import io.papermc.paper.event.player.PlayerArmSwingEvent
import me.aehz.uwuland.abstracts.GroupPerkListener
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.enums.SimonSaysObjective
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.lang.Error
import java.util.UUID

class SimonSays() : GroupPerkListener() {

    override var SETTING_taskDelay = 180 * 20..300 * 20
    var SETTING_objectiveTime: Long = 50
    var SETTING_instaKillPlayerThreshold = 3
    var SETTING_instaKillPercentage = 1.0f

    private val currentObjectives = mutableMapOf<UUID, SimonSaysObjective>()

    // Stores whether player has performed the relevant action
    private val currentObjectiveStates = mutableMapOf<UUID, Boolean>()

    private val debuffs = listOf(
        PotionEffect(PotionEffectType.WITHER, 13 * 20, 1),
        PotionEffect(PotionEffectType.POISON, 30 * 20, 0),
        PotionEffect(PotionEffectType.SLOW, 90 * 20, 1),
        PotionEffect(PotionEffectType.SLOW_DIGGING, 60 * 20, 0),
        PotionEffect(PotionEffectType.HUNGER, 120 * 20, 1),
        PotionEffect(PotionEffectType.DARKNESS, 30 * 20, 0),
    )

    private val buffs = listOf(
        PotionEffect(PotionEffectType.SPEED, 130 * 20, 0),
        PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60 * 20, 0),
        PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 90 * 20, 0),
        PotionEffect(PotionEffectType.REGENERATION, 35 * 20, 0),
        PotionEffect(PotionEffectType.ABSORPTION, 45 * 20, 1),
        PotionEffect(PotionEffectType.NIGHT_VISION, 300 * 20, 1),
    )

    override fun setup(owner: PerkOwner): Boolean {
        startTask(owner)
        return true
    }

    override fun task(targets: MutableList<LivingEntity>) {
        if (SETTING_taskDelay.first < 200 + SETTING_objectiveTime) throw Error("SimonSays TaskDelay can not be lower than 200 ticks + ObjectiveTime")

        targets.forEach {
            if (it !is Player) return
            it.sendTitle("", "NEW OBJECTIVE:", 10, 50, 10)
            it.sendMessage("§5NEW OBJECTIVE:")
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, Runnable {
                sendObjective(it)
            }, 82)
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, Runnable {
            handlePayout(targets)
        }, SETTING_objectiveTime + 82)
    }

    private fun sendObjective(player: Player) {
        val objective = SimonSaysObjective.values().random()
        val title = getTitle(objective)
        player.sendMessage("§5$title")
        player.sendTitle("", title, 5, SETTING_objectiveTime.toInt() - 10, 5)
        currentObjectives[player.uniqueId] = objective
        currentObjectiveStates[player.uniqueId] = false
    }

    private fun getTitle(objective: SimonSaysObjective): String {
        return if (objective.name.startsWith("DONT_")) {
            listOf<String>(
                "Simon says: DON'T ${objective.name.removePrefix("DONT_")}",
                objective.name.removePrefix("DONT_")
            ).random()
        } else {
            listOf<String>(
                "Simon says: ${objective.name}",
                "DON'T ${objective.name}"
            ).random()
        }
    }

    private fun handlePayout(targets: MutableList<LivingEntity>) {
        val failedPlayers = targets.filter { isFailedObjective(it) }

        targets.forEach { target ->
            calculateReward(targets.size, failedPlayers.size).forEach { potionEffect ->
                potionEffect.apply(target)
            }
            if (failedPlayers.isNotEmpty()) {
                target.sendMessage(
                    "§cThe following players failed their task:\n${
                        failedPlayers.map { it.name }.reduce { acc, s -> "$s\n" }
                    }"
                )
            }
            currentObjectives.remove(target.uniqueId)
        }
    }

    private fun isFailedObjective(player: LivingEntity): Boolean {
        if (!currentObjectives.containsKey(player.uniqueId)) return true
        if (!currentObjectiveStates.containsKey(player.uniqueId)) return true
        val objective = currentObjectives[player.uniqueId]
        if (objective!!.name.startsWith("DONT_")) {
            return currentObjectiveStates[player.uniqueId]!!
        } else {
            return !currentObjectiveStates[player.uniqueId]!!
        }
    }

    private fun calculateReward(targetAmount: Int, failedAmount: Int): List<PotionEffect> {
        val failedPercentage = failedAmount.toFloat() / targetAmount
        val isInstantKill =
            targetAmount >= SETTING_instaKillPlayerThreshold && failedPercentage >= SETTING_instaKillPercentage

        val instantKill = listOf(PotionEffect(PotionEffectType.HARM, 10, 10))

        return when {
            isInstantKill -> instantKill
            failedPercentage >= 0.5f -> debuffs.shuffled().subList(0, 2)
            failedPercentage > 0f -> debuffs.shuffled().subList(0, 1)
            else -> buffs.shuffled().subList(0, 2)
        }
    }

    @EventHandler
    fun onJump(e: EntityJumpEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.entity)) return
        val player = e.entity
        if (player !is Player) return
        objectiveCheck(player, SimonSaysObjective.JUMP)
        objectiveCheck(player, SimonSaysObjective.DONT_JUMP)
    }

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.player)) return
        if (e.hasChangedPosition()) {
            objectiveCheck(e.player, SimonSaysObjective.MOVE)
            objectiveCheck(e.player, SimonSaysObjective.DONT_MOVE)
        }
        if (e.hasChangedOrientation()) {
            objectiveCheck(e.player, SimonSaysObjective.LOOK)
            objectiveCheck(e.player, SimonSaysObjective.DONT_LOOK)
        }
    }

    @EventHandler
    fun onPunch(e: PlayerArmSwingEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.player)) return
        objectiveCheck(e.player, SimonSaysObjective.PUNCH)
        objectiveCheck(e.player, SimonSaysObjective.DONT_PUNCH)
    }

    @EventHandler
    fun onChat(e: AsyncChatEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.player)) return
        objectiveCheck(e.player, SimonSaysObjective.CHAT)
        objectiveCheck(e.player, SimonSaysObjective.DONT_CHAT)
    }

    @EventHandler
    fun onFoodLevelChange(e: FoodLevelChangeEvent) {
        if (!isEnabled) return
        if (!hasPerk(e.entity)) return
        if (e.foodLevel < e.entity.foodLevel) return
        val player = e.entity
        if (player !is Player) return
        objectiveCheck(player, SimonSaysObjective.DONT_EAT)
    }

    private fun objectiveCheck(player: Player, objective: SimonSaysObjective) {
        if (!currentObjectives.containsKey(player.uniqueId)) return
        if (!currentObjectiveStates.containsKey(player.uniqueId)) return
        val currentObjective = currentObjectives[player.uniqueId]
        if (currentObjective == objective) {
            currentObjectiveStates[player.uniqueId] = true
        }
    }
}