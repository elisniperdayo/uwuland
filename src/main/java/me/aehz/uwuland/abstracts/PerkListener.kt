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
import java.lang.Error

abstract class PerkListener() : Listener {
    val plugin = PluginInstance.get()!!
    var isEnabled: Boolean = true
    open val type: ListenerType = ListenerType.PERK
    var stg: MutableMap<String, String> = mutableMapOf()
    var perkOwners: MutableList<PerkOwner> = mutableListOf()
    val alias = this.javaClass.name.substringAfterLast(".")
    open var SETTING_taskDelay: IntRange = -1..-1

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
        val props = this.javaClass.kotlin.members
        val propNames = mutableListOf<String>()
        for (prop in props) {
            propNames.add(prop.name)
        }
        return propNames
    }

    fun setStg(key: String, value: String): Boolean {
        if (!this.stg.containsKey(key)) return false
        this.stg[key] = value
        return true
    }

    open fun setup(owner: PerkOwner): Boolean {
        return true
    }

    open fun unsetup(owner: PerkOwner) {
        return
    }

    fun add(groupAlias: String, targets: MutableList<LivingEntity>) {
        val uniqueIdList = targets.map { it.uniqueId }.toMutableList()
        val combinedUniqueIdString = uniqueIdList.sorted().toString()
        if (perkOwners.find { it.combinedUniqueIdString == combinedUniqueIdString } != null) return
        val owner = PerkOwner(PerkOwnerType.PLAYER, groupAlias, uniqueIdList, combinedUniqueIdString)
        val successfulSetup = setup(owner)
        if (!successfulSetup) return
        perkOwners.add(owner)
    }

    fun hasPerk(entity: Entity): Boolean {
        val team = Bukkit.getScoreboardManager().mainScoreboard.getEntityTeam(entity)?.name
        return perkOwners.find { it.targets.contains(entity.uniqueId) || it.groupAlias == team } != null
    }

    fun hasPerkByName(name: String): Boolean {
        return perkOwners.find { it.getTargetsAsLivingEntities().any { it.name == name && it is Player } } != null
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
                        .getTeam(it.groupAlias)
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
        if (SETTING_taskDelay.first == -1) throw Error("ATTEMPTED TO START TASK WITHOUT OVERRIDING DELAY")
        val targets = if (owner.type == PerkOwnerType.PLAYER) {
            owner.getTargetsAsLivingEntities()
        } else {
            val teamName = owner.groupAlias.substringAfter(":")
            Bukkit.getScoreboardManager().mainScoreboard.getTeam(teamName)?.entries?.mapNotNull { Bukkit.getPlayer(it) }
                ?.toMutableList<LivingEntity>() ?: mutableListOf()
        }

        val d = SETTING_taskDelay.random().toLong()
        Bukkit.getLogger().info("$d")
        owner.taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, Runnable {
            if (this.isEnabled && targets.isNotEmpty()) task(targets)
            startTask(owner)
        }, d)
    }

    private fun stopTask(groupAlias: String) {
        val taskId = perkOwners.find { it.groupAlias == groupAlias }?.taskId ?: return
        Bukkit.getScheduler().cancelTask(taskId)
    }
}