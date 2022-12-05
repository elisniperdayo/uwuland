package me.aehz.uwuland.interfaces

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.enums.PerkOwnerType
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.util.UUID

interface PerkListener : Listener {
    val plugin: Uwuland
    val type: ListenerType
    var isEnabled: Boolean
    var stg: MutableMap<String, String>
    var perkOwners: MutableList<PerkOwner>

    fun getAlias(): String {
        return this.javaClass.name.substringAfterLast(".")
    }

    fun enable() {
        isEnabled = true
    }

    fun disable() {
        isEnabled = false
    }

    fun setStg(key: String, value: String): Boolean {
        if (!this.stg.containsKey(key)) return false
        this.stg[key] = value
        return true
    }

    fun setup(owner: PerkOwner): Boolean {
        return true
    }

    fun add(groupAlias: String, targets: MutableList<LivingEntity>) {
        val combinedUniqueIdString = targets.map { it.uniqueId }.sorted().toString()
        if (perkOwners.find { it.combinedUniqueIdString == combinedUniqueIdString } != null) return
        val owner = PerkOwner(PerkOwnerType.PLAYER, groupAlias, targets, combinedUniqueIdString)
        val successfulSetup = setup(owner)
        if (!successfulSetup) return
        perkOwners.add(owner)
    }

    fun hasPerk(entity: Entity): Boolean {
        val team = Bukkit.getScoreboardManager().mainScoreboard.getEntityTeam(entity)?.name
        return perkOwners.find { it.targets.contains(entity) || it.groupAlias == team } != null
    }

    fun hasPerkByName(name: String): Boolean {
        return perkOwners.find { it.targets.any { it.name == name && it is Player } } != null
    }

    fun remove(groupAlias: String) {
        if (this is TimedPerk) {
            this.stopTask(groupAlias)
        }
        perkOwners.removeIf { it.groupAlias == groupAlias }
    }

    fun getPartners(entity: Entity): List<LivingEntity> {
        val partners = mutableSetOf<LivingEntity>()
        perkOwners.forEach {
            it.clean()
            when (it.type) {
                PerkOwnerType.PLAYER -> {
                    if (it.targets.contains(entity)) partners.addAll(it.targets)
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

    fun getOwner(groupAlias: String): PerkOwner? {
        return perkOwners.find { it.groupAlias == groupAlias }
    }
}