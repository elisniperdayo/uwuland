package me.aehz.uwuland.API.Data

import org.bukkit.attribute.AttributeModifier
import org.bukkit.potion.PotionEffect
import java.util.*

data class ApiDataEntity(
    val id: UUID,
    val name: String,
    val level: Int,
    val health: Double,
    val perks: List<ApiDataListener>,
    val potionEffects: Collection<PotionEffect>,
    val attributes: List<ApiDataEntityAttributeData>,
)

data class ApiDataEntityAttributeData(
    val name: String,
    val baseValue: Double?,
    val value: Double?,
    val modifiers: Collection<AttributeModifier>
)