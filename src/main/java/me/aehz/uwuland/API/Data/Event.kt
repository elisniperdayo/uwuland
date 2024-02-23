package me.aehz.uwuland.API.Data

import me.aehz.uwuland.enums.ApiEventType
import org.bukkit.util.Vector
import java.util.UUID

data class ApiEvent(
    val type: ApiEventType,
    val data: ApiEventData,
    val timeStamp: Long = System.currentTimeMillis(),
)

sealed class ApiEventData {
    data class Damage(
        val location: Vector,
        val entity: UUID,
        val amount: Number,
        val cause: String
    ) : ApiEventData()
}