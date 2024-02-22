package me.aehz.uwuland.API.Controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import me.aehz.uwuland.API.Data.*
import me.aehz.uwuland.abstracts.SharedSseController
import me.aehz.uwuland.managers.EventManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Team

object TeamController : SharedSseController() {

    override fun getSseData(): AllTeamsData {
        val teams = Bukkit.getScoreboardManager().mainScoreboard.teams.map {
            teamToTeamData(it)
        }
        return AllTeamsData(teams)
    }

    // Add team
    suspend fun post(call: ApplicationCall) {
        val teamData = call.receive<TeamUpdateData>()
        val team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam(call.parameters["teamName"]!!)

        updateTeam(team, teamData)

        call.respondText("Team created successfully", status = HttpStatusCode.Created)
    }

    // Edit team
    suspend fun put(call: ApplicationCall) {
        val teamData = call.receive<TeamUpdateData>()
        val team = Bukkit.getScoreboardManager().mainScoreboard.getTeam(call.parameters["teamName"]!!)
        if (team == null) {
            call.respondText("Team not found", status = HttpStatusCode.NotFound)
            return
        }

        updateTeam(team, teamData)

        call.respondText("Team updated successfully", status = HttpStatusCode.OK)
    }

    // Delete Team
    suspend fun delete(call: ApplicationCall) {
        val teamName = call.parameters["teamName"]
        Bukkit.getScoreboardManager().mainScoreboard.getTeam(teamName!!)?.unregister()
        call.respondText("Team deleted successfully", status = HttpStatusCode.NoContent)
    }

    // Get all colors
    suspend fun getColor(call: ApplicationCall) {
        call.respond(NamedTextColor.NAMES)
    }

    private fun teamToTeamData(team: Team): TeamData {
        return TeamData(
            team.name,
            team.prefix().toString(),
            team.color(),
            team.entries,
            EventManager.getPerksByTeam(team).map {
                TeamPerkData(
                    it.alias,
                    it.isEnabled
                )
            }
        )
    }

    private fun updateTeam(team: Team, teamData: TeamUpdateData) {

        if (teamData.prefix != null) {
            team.prefix(Component.text("[${teamData.prefix}] "))
        }

        if (teamData.color != null) {
            team.color(NamedTextColor.NAMES.value(teamData.color))
        }

        team.addEntries(teamData.members)
    }
}
