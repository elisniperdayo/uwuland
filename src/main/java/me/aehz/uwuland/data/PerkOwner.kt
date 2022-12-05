package me.aehz.uwuland.data

import me.aehz.uwuland.enums.PerkOwnerType
import org.bukkit.entity.LivingEntity

data class PerkOwner(
    val type: PerkOwnerType,
    val groupAlias: String,
    val targets: MutableList<LivingEntity> = mutableListOf(),
    val combinedUniqueIdString: String = ""
) {
    var taskId: Int = -1
    var cooldown: Long = System.currentTimeMillis() / 1000

    fun clean() {
        this.targets.removeIf { it.isDead }
    }
}
