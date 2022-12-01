package me.aehz.uwuland.data

import org.bukkit.entity.LivingEntity

data class PerkOwner(
    val groupAlias: String, val targets: MutableList<LivingEntity>, val combinedUniqueIdString: String
) {
    var taskId: Int = -1
}
