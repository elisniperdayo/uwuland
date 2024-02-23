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
        val entity: ApiEventEntity,
        val amount: Number,
        val cause: String
    ) : ApiEventData()

    data class Pvp(
        val entity: ApiEventEntity,
        val damager: ApiEventEntity,
        val amount: Number,
        val cause: String,
    ) : ApiEventData()

    data class Death(
        val entity: ApiEventEntity,
    ) : ApiEventData()

    data class JoinQuit(
        val entity: ApiEventEntity,
        val ping: Int,
    ) : ApiEventData()

}

data class ApiEventEntity(
    val id: UUID,
    val name: String,
    val location: Vector,
    val entityHp: Long,
    val level: Int,
)