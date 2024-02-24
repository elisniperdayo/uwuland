package me.aehz.uwuland.API.Data

import me.aehz.uwuland.enums.ApiEventType
import org.bukkit.enchantments.Enchantment
import org.bukkit.util.Vector
import java.util.UUID


sealed class ApiDataEvent {
    abstract val type: ApiEventType
    val timeStamp: Long = System.currentTimeMillis()

    data class Damage(
        val entity: ApiDataEntity,
        val amount: Number,
        val cause: String,
        override val type: ApiEventType = ApiEventType.DAMAGE,
    ) : ApiDataEvent()

    data class Pvp(
        val entity: ApiDataEntity,
        val damager: ApiDataEntity,
        val amount: Number,
        val cause: String,
        override val type: ApiEventType = ApiEventType.PVP,
    ) : ApiDataEvent()

    data class Death(
        val entity: ApiDataEntity,
        override val type: ApiEventType = ApiEventType.DEATH,
    ) : ApiDataEvent()

    data class Join(
        val entity: ApiDataEntity,
        val ping: Int,
        override val type: ApiEventType = ApiEventType.JOIN,
    ) : ApiDataEvent()

    data class Quit(
        val entity: ApiDataEntity,
        val ping: Int,
        override val type: ApiEventType = ApiEventType.QUIT,
    ) : ApiDataEvent()

    data class Portal(
        val entity: ApiDataEntity,
        val from: ApiDataLocation,
        val to: ApiDataLocation,
        override val type: ApiEventType = ApiEventType.PORTAL,
    ) : ApiDataEvent()

    data class Enchant(
        val entity: ApiDataEntity,
        val enchants: Any,
        val itemName: String,
        override val type: ApiEventType = ApiEventType.ENCHANT,
    ) : ApiDataEvent()
}
