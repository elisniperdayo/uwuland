package me.aehz.uwuland.API.Data

import net.kyori.adventure.text.format.TextColor

data class AllTeamsData(
    val teams: List<TeamData>
)

data class TeamData(
    val name: String,
    val prefix: String,
    val color: TextColor,
    val members: Set<String>,
    val perks: List<TeamPerkData>,
)

data class TeamPerkData(
    val name: String,
    val isEnabled: Boolean,
)

data class TeamUpdateData(
    val prefix: String?,
    val color: String?,
    val members: List<String>,
)