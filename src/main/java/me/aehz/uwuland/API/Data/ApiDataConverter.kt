package me.aehz.uwuland.API.Data

import com.google.gson.Gson
import me.aehz.uwuland.abstracts.PerkListener
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.enums.PerkOwnerType
import me.aehz.uwuland.managers.EventManager
import net.kyori.adventure.text.TextComponent
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.scoreboard.Team

object ApiDataConverter {

    //region MISC
    fun location(loc: Location): ApiDataLocation {
        return ApiDataLocation(
            loc.x,
            loc.y,
            loc.z,
            loc.world.environment.name
        )
    }

    fun itemStack(itemStack: ItemStack): ApiDataItemStack {
        return ApiDataItemStack(
            itemStack.type.name,
            itemStack.amount,
            enchantmentMapToList(itemStack.enchantments)
        )
    }

    fun enchantmentMapToList(map: Map<Enchantment, Int>): List<ApiDataEnchantment> {
        return map.map { ApiDataEnchantment(it.key.key.key, it.value) }
    }
    //endregion

    //region ENTITY
    fun entity(e: Entity): ApiDataEntity {
        val health = if (e is LivingEntity) e.health.toDouble() else -1.0
        val level = if (e is Player) e.expToLevel else -1
        val activePotionEffects = if (e is LivingEntity) e.activePotionEffects else listOf<PotionEffect>()
        val attributes = if (e is LivingEntity) attributeList(e) else listOf<ApiDataEntityAttributeData>()

        return ApiDataEntity(
            e.uniqueId,
            e.name,
            level,
            health,
            appliedPerkListEntity(e),
            activePotionEffects,
            attributes,
        )
    }

    private fun appliedPerkListEntity(e: Entity): List<ApiDataListener> {
        return EventManager.getPerksByEntity(e).map {
            it.perkOwners.filter { it.targets.contains(e.uniqueId) && it.type == PerkOwnerType.PLAYER }
            listener(it, false)
        }
    }

    private fun attributeList(e: LivingEntity): List<ApiDataEntityAttributeData> {
        return Attribute.values().mapNotNull { v -> e.getAttribute(v) }.map { attr ->
            ApiDataEntityAttributeData(
                attr.attribute.name,
                attr.baseValue,
                attr.value,
                attr.modifiers,
            )
        }
    }
    //endregion

    //region TEAM
    fun team(team: Team): ApiDataTeam {
        val listeners = EventManager.getPerksByTeam(team)
        return ApiDataTeam(
            team.name,
            (team.prefix() as TextComponent).content(),
            team.color(),
            team.entries,
            listeners.map { listener(it, false) }
        )
    }

    //endregion

    //region LISTENER
    fun listener(listener: PerkListener, getSettings: Boolean = true): ApiDataListener {
        return ApiDataListener(
            listener.alias,
            listener.type,
            listener.perkOwners.map { perkOwner(it) },
            if (getSettings) settingList(listener) else listOf(),
            listener.isEnabled
        )
    }

    private fun settingList(listener: PerkListener): List<ApiDataPerkSetting> {
        return listener.getSettings().map {
            val field = listener::class.java.getDeclaredField("SETTING_${it}")
            field.trySetAccessible()
            val value = field.get(listener)
            ApiDataPerkSetting(it, value, getSettingTypeFromValue(value))
        }
    }

    private fun perkOwner(owner: PerkOwner): ApiDataPerkOwner {
        return ApiDataPerkOwner(
            owner.groupAlias,
            owner.targets,
            owner.type,
            owner.combinedUniqueIdString
        )
    }

    private fun getSettingTypeFromValue(value: Any): ApiDataSettingType {
        return when (value) {
            is Int -> ApiDataSettingType.INT
            is Double -> ApiDataSettingType.DOUBLE
            is Long -> ApiDataSettingType.LONG
            is Float -> ApiDataSettingType.FLOAT
            is Boolean -> ApiDataSettingType.BOOLEAN
            is IntRange -> ApiDataSettingType.INTRANGE
            is String -> ApiDataSettingType.STRING
            else -> ApiDataSettingType.NULL
        }
    }

    fun convertValueToSettingType(value: Any, type: ApiDataSettingType): Any? {
        return when {
            value is Number && type == ApiDataSettingType.INT -> value.toInt()
            value is Number && type == ApiDataSettingType.DOUBLE -> value.toDouble()
            value is Number && type == ApiDataSettingType.LONG -> value.toLong()
            value is Number && type == ApiDataSettingType.FLOAT -> value.toFloat()
            value is Boolean -> value
            value is String -> value
            type == ApiDataSettingType.INTRANGE -> {
                Gson().fromJson(value.toString(), IntRange::class.java)
            }

            else -> null
        }
    }
    //endregion
}