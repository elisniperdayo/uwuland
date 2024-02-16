package me.aehz.uwuland.API.Data

data class AllTeamsData(
    val teams: List<TeamData>
)

data class TeamData(
    val name: String,
    val size: Int,
    val color: String,
    val members: Set<String>,
    val perks: List<TeamPerkData>,
)

data class TeamPerkData(
    val name: String,
    val isEnabled: Boolean,
)