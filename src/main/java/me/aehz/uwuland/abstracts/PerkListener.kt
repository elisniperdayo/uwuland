package me.aehz.uwuland.abstracts

import me.aehz.uwuland.PluginInstance
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.enums.PerkOwnerType
import me.aehz.uwuland.managers.EventManager
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.scoreboard.Team
import java.lang.Error
import java.util.UUID

abstract class PerkListener() : Listener {
    val plugin = PluginInstance.get()!!
    var isEnabled: Boolean = true
    open val type: ListenerType = ListenerType.PERK
    var perkOwners: MutableList<PerkOwner> = mutableListOf()
    val alias = this.javaClass.name.substringAfterLast(".")
    open var SETTING_taskDelay: IntRange = -1..-1
    open var SETTING_cooldown = -1

    init {
        registerEvents()
    }

    private fun registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, ListenerType.PERK)
    }

    fun enable() {
        isEnabled = true
    }

    fun disable() {
        isEnabled = false
    }

    fun getSettings(): MutableList<String> {
        val fields = this::class.java.declaredFields
        val settingFields = fields.filter { it.name.startsWith("SETTING_") }
        val settingFieldNames = settingFields.map { it.name }
        return settingFieldNames.map { it.removePrefix("SETTING_") }.toMutableList()
    }

    fun setSettings(name: String, value: Any): Boolean {
        val field = this::class.java.getDeclaredField("SETTING_${name}")
        if (!field.trySetAccessible()) return false
        val fieldValue = field.get(this)
        Bukkit.getLogger().info("${value::class}")
        if (fieldValue::class != value::class) return false
        field.set(this, value)
        return true
    }

    open fun setup(owner: PerkOwner): Boolean {
        return true
    }

    open fun unsetup(owner: PerkOwner) {
        return
    }

    fun add(groupAlias: String, targets: MutableList<UUID>) {
        val combinedUniqueIdString = targets.sorted().toString()
        if (perkOwners.find { it.combinedUniqueIdString == combinedUniqueIdString } != null) return
        val owner = PerkOwner(PerkOwnerType.PLAYER, groupAlias, targets, combinedUniqueIdString)
        val successfulSetup = setup(owner)
        if (!successfulSetup) return
        perkOwners.add(owner)
    }

    fun hasPerk(entity: Entity): Boolean {
        val teamName = Bukkit.getScoreboardManager().mainScoreboard.getEntityTeam(entity)?.name
        return perkOwners.find { it.targets.contains(entity.uniqueId) || it.groupAlias == "TEAM:$teamName" } != null
    }

    fun hasPerkTeam(team: Team): Boolean {
        return perkOwners.find { it.groupAlias == "TEAM:${team.name}" } != null
    }

    fun hasPerkByName(name: String): Boolean {
        return perkOwners.find { it.getTargetsAsLivingEntities().any { it.name == name && it is Player } } != null
    }

    fun handleCooldown(e: Entity): Boolean {
        if (this.SETTING_cooldown <= 0) {
            Bukkit.getLogger().info("Can not use handleCooldown with cooldown lower than 1. In $alias")
            return false
        }
        val owner = getOwner(e.name) ?: return false
        if (owner.isOnCooldown(this.SETTING_cooldown)) return false
        owner.updateCooldown(this.SETTING_cooldown)
        return true
    }

    fun remove(groupAlias: String) {
        this.stopTask(groupAlias)
        val owner = this.getOwner(groupAlias) ?: return
        this.unsetup(owner)
        perkOwners.removeIf { it.groupAlias == groupAlias }
    }

    fun getOwner(groupAlias: String): PerkOwner? {
        return perkOwners.find { it.groupAlias == groupAlias }
    }

    fun getPartners(entity: Entity): List<LivingEntity> {
        val partners = mutableSetOf<LivingEntity>()
        perkOwners.forEach {
            when (it.type) {
                PerkOwnerType.PLAYER -> {
                    if (it.targets.contains(entity.uniqueId)) partners.addAll(it.getTargetsAsLivingEntities())
                }

                PerkOwnerType.TEAM -> {
                    val players = Bukkit
                        .getScoreboardManager()
                        .mainScoreboard
                        .getTeam(it.groupAlias.removePrefix("TEAM:"))
                        ?.entries
                        ?.mapNotNull {
                            Bukkit.getPlayer(
                                it
                            )
                        }?.toSet<LivingEntity>() ?: setOf()
                    partners.addAll(players)
                }
            }
        }
        return partners.filter { it != entity }
    }

    //Functions for timed tasks
    open fun task(targets: MutableList<LivingEntity>) {}

    fun startTask(owner: PerkOwner) {
        if (SETTING_taskDelay.first <= -1) throw Error("ATTEMPTED TO START TASK WITHOUT OVERRIDING DELAY OR NEGATIVE DELAY")
        val targets = owner.getTargetsAsLivingEntities()

        val delay = SETTING_taskDelay.random().toLong()

        owner.taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, Runnable {
            if (this.isEnabled && targets.isNotEmpty()) task(targets)
            startTask(owner)
        }, delay)
    }

    private fun stopTask(groupAlias: String) {
        val taskId = perkOwners.find { it.groupAlias == groupAlias }?.taskId ?: return
        Bukkit.getScheduler().cancelTask(taskId)
    }
}