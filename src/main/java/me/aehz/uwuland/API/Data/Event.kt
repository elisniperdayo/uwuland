package me.aehz.uwuland.API.Data

import me.aehz.uwuland.enums.ApiEventOrigin
import me.aehz.uwuland.enums.ApiEventType
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.util.Vector
import java.util.UUID


sealed class ApiDataEvent {
    abstract val eventType: ApiEventType
    abstract val eventOrigin: ApiEventOrigin
    val timeStamp: Long = System.currentTimeMillis()

    sealed class Minecraft : ApiDataEvent() {
        override val eventOrigin: ApiEventOrigin = ApiEventOrigin.MINECRAFT

        data class Damage(
            val entity: ApiDataEntity,
            val amount: Number,
            val cause: String,
            override val eventType: ApiEventType = ApiEventType.DAMAGE,
        ) : Minecraft()

        data class Pvp(
            val entity: ApiDataEntity,
            val damager: ApiDataEntity,
            val amount: Number,
            val cause: String,
            override val eventType: ApiEventType = ApiEventType.PVP,
        ) : Minecraft()

        data class Death(
            val entity: ApiDataEntity,
            override val eventType: ApiEventType = ApiEventType.DEATH,
        ) : Minecraft()

        data class Join(
            val entity: ApiDataEntity,
            val ping: Int,
            override val eventType: ApiEventType = ApiEventType.JOIN,
        ) : Minecraft()

        data class Quit(
            val entity: ApiDataEntity,
            val ping: Int,
            override val eventType: ApiEventType = ApiEventType.QUIT,
        ) : Minecraft()

        data class Portal(
            val entity: ApiDataEntity,
            val from: ApiDataLocation,
            val to: ApiDataLocation,
            override val eventType: ApiEventType = ApiEventType.PORTAL,
        ) : Minecraft()

        data class Enchant(
            val entity: ApiDataEntity,
            val itemStack: ApiDataItemStack,
            val enchants: List<ApiDataEnchantment>,
            override val eventType: ApiEventType = ApiEventType.ENCHANT,
        ) : Minecraft()

        data class EnderEye(
            val entity: ApiDataEntity,
            override val eventType: ApiEventType = ApiEventType.ENDER_EYE,
        ) : Minecraft()
    }

    sealed class Perk : ApiDataEvent() {
        override val eventOrigin: ApiEventOrigin = ApiEventOrigin.PERK

        data class Shuffle(
            val entities: List<ApiDataEntity>,
            override val eventType: ApiEventType = ApiEventType.SHUFFLE,
        ) : Perk()

        data class Disorganized(
            val entities: ApiDataEntity,
            val itemStack: ApiDataItemStack,
            override val eventType: ApiEventType,
        ) : Perk()
    }
}
