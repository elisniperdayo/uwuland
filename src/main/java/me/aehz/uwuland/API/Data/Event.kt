package me.aehz.uwuland.API.Data

import me.aehz.uwuland.enums.ApiEventType
import org.bukkit.util.Vector
import java.util.UUID


sealed class ApiDataEvent {
    abstract val type: ApiEventType
    val timeStamp: Long = System.currentTimeMillis()

    data class Damage(
        override val type: ApiEventType,
        val entity: ApiDataEntity,
        val amount: Number,
        val cause: String
    ) : ApiDataEvent()

    data class Pvp(
        override val type: ApiEventType,
        val entity: ApiDataEntity,
        val damager: ApiDataEntity,
        val amount: Number,
        val cause: String,
    ) : ApiDataEvent()

    data class Death(
        override val type: ApiEventType,
        val entity: ApiDataEntity,
    ) : ApiDataEvent()

    data class JoinQuit(
        override val type: ApiEventType,
        val entity: ApiDataEntity,
        val ping: Int,
    ) : ApiDataEvent()

    data class Portal(
        override val type: ApiEventType,
        val entity: ApiDataEntity,
        val from: ApiDataLocation,
        val to: ApiDataLocation,
    ) : ApiDataEvent()
}
