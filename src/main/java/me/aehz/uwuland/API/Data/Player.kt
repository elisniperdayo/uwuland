package me.aehz.uwuland.API.Data

import org.bukkit.attribute.AttributeModifier
import org.bukkit.potion.PotionEffect
import java.util.UUID

data class AllPlayersData(
    val players: List<PlayerData>,
)

data class PlayerData(
    val id: UUID,
    val name: String,
    val level: Int,
    val health: Double,
    val perks: List<PlayerPerkData>,
    val potionEffects: Collection<PotionEffect>,
    val attributes: Any
)

data class PlayerAttributeData(
    val name: String,
    val baseValue: Double?,
    val value: Double?,
    val modifiers: Collection<AttributeModifier>
)

data class PlayerPerkData(
    val name: String,
    val isEnabled: Boolean,
    val partners: Any
)