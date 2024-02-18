package me.aehz.uwuland.API.Data

import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.enums.PerkOwnerType
import java.util.UUID

data class AllPerksData(
    val perks: List<ListenerData>
)

data class ListenerData(
    val name: String,
    val type: ListenerType,
    val owners: List<PerkOwner>,
    val settings: List<SettingData>,
)

data class SettingData(
    val name: String,
    val value: Any,
    val type: SettingDataType
)

data class PostOwnerData(
    val type: PerkOwnerType,
    val groupAlias: String,
    val targets: MutableList<UUID>
)

enum class SettingDataType {
    INT, DOUBLE, LONG, FLOAT, BOOLEAN, INTRANGE, STRING, NULL
}