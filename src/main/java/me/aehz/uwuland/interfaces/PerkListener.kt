package me.aehz.uwuland.interfaces

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.enums.ListenerType
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

    fun setup(targets: MutableList<LivingEntity>): Boolean {
        return true
    }

    fun add(groupAlias: String, targets: MutableList<LivingEntity>) {
        val combinedUniqueIdString = targets.map { it.uniqueId }.sorted().toString()
        if (perkOwners.find { it.combinedUniqueIdString == combinedUniqueIdString } != null) return
        val successfulSetup = setup(targets)
        if (!successfulSetup) return
        perkOwners.add(PerkOwner(groupAlias, targets, combinedUniqueIdString))
    }

    fun hasPerk(entity: Entity): Boolean {
        return perkOwners.find { it.targets.contains(entity) } != null
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
}