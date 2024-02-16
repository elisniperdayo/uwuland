package me.aehz.uwuland.API.Controllers

import com.google.gson.Gson
import io.ktor.server.application.*
import io.ktor.server.response.*
import me.aehz.uwuland.API.Data.TeamData
import me.aehz.uwuland.API.Data.TeamPerkData
import me.aehz.uwuland.managers.EventManager
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Team

object TeamController {
    suspend fun get(call: ApplicationCall) {
        val teams = Bukkit.getScoreboardManager().mainScoreboard.teams.map {
            teamToTeamData(it)
        }

        Bukkit.getScoreboardManager().mainScoreboard.teams.map {
            Bukkit.getLogger().info("${it.entries}")
        }

        val responseData = teams
        val response = Gson().toJson(responseData)
        call.respond(response)
    }

    private fun teamToTeamData(team: Team): TeamData {
        return TeamData(
            team.name,
            team.size,
            team.color().asHexString(),
            team.entries,
            EventManager.getPerksByTeam(team).map {
                TeamPerkData(
                    it.alias,
                    it.isEnabled
                )
            }
        )
    }
}
