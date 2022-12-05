package me.aehz.uwuland.data

import me.aehz.uwuland.enums.PerkOwnerType
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import java.util.UUID

data class PerkOwner(
    val type: PerkOwnerType,
    val groupAlias: String,
    val targets: MutableList<UUID> = mutableListOf(),
    val combinedUniqueIdString: String = ""
) {
    var taskId: Int = -1
    var cooldown: Long = System.currentTimeMillis() / 1000

    fun getTargetsAsEntities(): MutableList<LivingEntity> {
        return targets.mapNotNull { Bukkit.getEntity(it) }.filterIsInstance<LivingEntity>().toMutableList()
    }
}
