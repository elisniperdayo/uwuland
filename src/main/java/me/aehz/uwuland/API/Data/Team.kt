package me.aehz.uwuland.API.Data

import net.kyori.adventure.text.format.TextColor

data class ApiDataTeam(
    val name: String,
    val prefix: String,
    val color: TextColor,
    val members: Set<String>,
    val perks: List<ApiDataListener>,
)

data class UpdateTeamData(
    val prefix: String?,
    val color: String?,
    val members: List<String>,
)