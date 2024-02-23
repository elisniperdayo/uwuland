package me.aehz.uwuland.API.Data

import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.enums.PerkOwnerType
import java.util.UUID

data class ApiDataListener(
    val name: String,
    val type: ListenerType,
    val owners: List<ApiDataPerkOwner>,
    val settings: List<ApiDataPerkSetting>,
    val isEnabled: Boolean,
)

data class ApiDataPerkSetting(
    val name: String,
    val value: Any,
    val type: ApiDataSettingType
)

data class ApiDataPerkOwner(
    val groupAlias: String,
    val targets: MutableList<UUID>,
    val type: PerkOwnerType,
    val combinedUniqueIdString: String
)

enum class ApiDataSettingType {
    INT, DOUBLE, LONG, FLOAT, BOOLEAN, INTRANGE, STRING, NULL
}

data class ApiDataPostOwner(
    val type: PerkOwnerType,
    val groupAlias: String,
    val targets: MutableList<UUID>
)