package me.aehz.uwuland.API.Controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import me.aehz.uwuland.API.Data.*
import me.aehz.uwuland.API.Data.ApiDataConverter
import me.aehz.uwuland.abstracts.GroupPerkListener
import me.aehz.uwuland.abstracts.SharedSseController
import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.enums.PerkOwnerType
import me.aehz.uwuland.managers.EventManager

object PerkController : SharedSseController() {
    override fun getSseData(): List<ApiDataListener> {
        val perks = EventManager.listeners.values.map {
            ApiDataConverter.listener(it)
        }
        return perks
    }

    suspend fun putSettings(call: ApplicationCall) {
        val updateData = call.receive<ApiDataPerkSetting>()
        val listener = EventManager.listeners.values.find { it.alias == call.parameters["perk"] }
        val value = ApiDataConverter.convertValueToSettingType(updateData.value, updateData.type)
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
        val ownerData = call.receive<ApiDataPostOwner>()
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

}
