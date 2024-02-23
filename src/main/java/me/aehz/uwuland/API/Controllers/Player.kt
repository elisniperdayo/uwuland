package me.aehz.uwuland.API.Controllers

import me.aehz.uwuland.API.Data.ApiDataConverter
import me.aehz.uwuland.API.Data.ApiDataEntity
import me.aehz.uwuland.abstracts.SharedSseController
import org.bukkit.Bukkit

object PlayerController : SharedSseController() {
    override fun getSseData(): List<ApiDataEntity> {
        val players = Bukkit.getServer().onlinePlayers.map {
            ApiDataConverter.entity(it)
        }
        return players
    }
}
