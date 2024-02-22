package me.aehz.uwuland.API.Controllers

import com.google.gson.Gson
import com.typesafe.config.ConfigException.Null
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import me.aehz.uwuland.API.Data.*
import me.aehz.uwuland.abstracts.GroupPerkListener
import me.aehz.uwuland.abstracts.PerkListener
import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.enums.PerkOwnerType
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.util.ApiUtil

object PerkController {
    suspend fun get(call: ApplicationCall) {

        call.respondTextWriter(ContentType.Text.EventStream, HttpStatusCode.OK) {
            while (true) {
                val perks = EventManager.listeners.values.map {
                    listenerToListenerData(it)
                }

                val responseData = AllPerksData(perks)
                ApiUtil.asJsonSSE(this, 1000, responseData)
            }
        }
    }

    suspend fun putSettings(call: ApplicationCall) {
        val updateData = call.receive<SettingData>()
        val listener = EventManager.listeners.values.find { it.alias == call.parameters["perk"] }
        val value = valueToSettingType(updateData.value, updateData.type)
        if (listener == null) {
            call.respondText("Could not find perk", status = HttpStatusCode.NotFound)
            return
        }
        if (value == null) {
            call.respondText("Value is null", status = HttpStatusCode.BadRequest)
            return
        }

        listener.setSettings(updateData.name, value)
        call.respondText("Setting changed successfully", status = HttpStatusCode.OK)
    }

    suspend fun postOwner(call: ApplicationCall) {
        val ownerData = call.receive<PostOwnerData>()
        val listener = EventManager.listeners.values.find { it.alias == call.parameters["perk"] }

        if (listener == null) {
            call.respondText("Could not find perk", status = HttpStatusCode.NotFound)
            return
        }

        if (listener.type == ListenerType.PERK && ownerData.type == PerkOwnerType.TEAM) {
            call.respondText("Perk type mismatch", status = HttpStatusCode.BadRequest)
            return
        }

        if (ownerData.type == PerkOwnerType.TEAM && ownerData.targets.isEmpty()) {
            call.respondText("No targets", status = HttpStatusCode.BadRequest)
            return
        }

        if (ownerData.type == PerkOwnerType.TEAM && listener is GroupPerkListener) {
            listener.addTeam("TEAM:${ownerData.groupAlias}")
        } else {
            listener.add(ownerData.groupAlias, ownerData.targets)
        }

        call.respondText("Perk owner created successfully", status = HttpStatusCode.Created)
    }

    suspend fun deleteOwner(call: ApplicationCall) {
        val listener = EventManager.listeners.values.find { it.alias == call.parameters["perk"] }
        val groupAlias = call.parameters["groupAlias"]!!
        if (listener == null) {
            call.respondText("Could not find perk", status = HttpStatusCode.NotFound)
            return
        }
        listener.remove(groupAlias)
        call.respondText("Team deleted successfully", status = HttpStatusCode.NoContent)
    }

    private fun listenerToListenerData(listener: PerkListener): ListenerData {
        return ListenerData(
            listener.alias,
            listener.type,
            listener.perkOwners,
            listener.getSettings().map {
                val field = listener::class.java.getDeclaredField("SETTING_${it}")
                field.trySetAccessible()
                val value = field.get(listener)
                SettingData(it, value, getSettingTypeFromValue(value))
            },
        )
    }

    private fun getSettingTypeFromValue(value: Any): SettingDataType {
        return when (value) {
            is Int -> SettingDataType.INT
            is Double -> SettingDataType.DOUBLE
            is Long -> SettingDataType.LONG
            is Float -> SettingDataType.FLOAT
            is Boolean -> SettingDataType.BOOLEAN
            is IntRange -> SettingDataType.INTRANGE
            is String -> SettingDataType.STRING
            else -> SettingDataType.NULL
        }
    }

    private fun valueToSettingType(value: Any, type: SettingDataType): Any? {
        return when {
            value is Number && type == SettingDataType.INT -> value.toInt()
            value is Number && type == SettingDataType.DOUBLE -> value.toDouble()
            value is Number && type == SettingDataType.LONG -> value.toLong()
            value is Number && type == SettingDataType.FLOAT -> value.toFloat()
            value is Boolean -> value
            value is String -> value
            type == SettingDataType.INTRANGE -> {
                Gson().fromJson(value.toString(), IntRange::class.java)
            }

            else -> null
        }
    }
}
