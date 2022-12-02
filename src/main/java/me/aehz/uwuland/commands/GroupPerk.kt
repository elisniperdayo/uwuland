package me.aehz.uwuland.commands

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.interfaces.GroupPerkListener
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.util.MultiTabCompleterBuilder
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.LivingEntity


class GroupPerk(private val plugin: Uwuland) : CommandExecutor {
    init {
        plugin.getCommand("groupPerk")!!.setExecutor(this)
        plugin.getCommand("groupPerk")!!.tabCompleter =
            MultiTabCompleterBuilder()
                .addStringOptions(EventManager.listeners.filter { it.value.type != ListenerType.PERK }.keys.toMutableList())
                .addStringOptions(mutableListOf("add", "remove"))
                .addFunctionalOptions {
                    when (it[1]) {
                        "add" -> {
                            val teams = Bukkit.getScoreboardManager().mainScoreboard.teams.map { "TEAM:${it.name}" }
                            val players = Bukkit.getOnlinePlayers().map { "PLAYER:${it.name}" }
                            (teams + players).toMutableList()
                        }

                        "remove" -> {
                            val listener = EventManager.get(it[0])
                            if (listener is GroupPerkListener) {
                                val groups = listener?.perkOwners?.map { it.groupAlias } ?: mutableListOf()
                                val teams = listener.perkOwnerTeams.map { "TEAM:$it" }
                                (teams + groups).toMutableList()
                            } else {
                                mutableListOf()
                            }
                        }

                        else -> mutableListOf()
                    }
                }
                .enableInfinitePlayerOptions { !it[2].startsWith("TEAM:") }
                .create()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args!!.size < 3) return false
        val listener = EventManager.get(args[0]) ?: return false
        if (listener !is GroupPerkListener) return false

        when (args[2].startsWith("TEAM:")) {
            true -> {
                val team = args[2].substringAfterLast(":")
                when (args[1]) {
                    "add" -> {
                        listener.addTeam(team)
                    }

                    "remove" -> listener.removeTeam(team)
                    else -> return false
                }
            }

            false -> {
                val targets =
                    args.copyOfRange(2, args.size).mapNotNull { Bukkit.getPlayer(it.substringAfterLast(":")) }
                        .toSet<LivingEntity>()
                val groupAlias = targets.joinToString("&") { it.name }

                when (args[1]) {
                    "add" -> {
                        listener.add(groupAlias, targets.toMutableList())
                    }

                    "remove" -> listener.remove(groupAlias)
                    else -> return false
                }
            }
        }
        return true
    }
}