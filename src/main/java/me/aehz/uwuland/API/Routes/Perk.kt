package me.aehz.uwuland.API.Routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import me.aehz.uwuland.API.Controllers.PerkController

fun Route.perks() {
    get("perks/sse") {
        PerkController.get(call)
    }

    put("perks/{perk}/settings") {
        PerkController.putSettings(call)
    }

    post("perks/{perk}/owners") {
        PerkController.postOwner(call)
    }

    delete("perks/{perk}/owners/{groupAlias}") {
        PerkController.deleteOwner(call)
    }
}