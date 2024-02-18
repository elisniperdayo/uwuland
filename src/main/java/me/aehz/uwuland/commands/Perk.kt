package me.aehz.uwuland.commands

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.util.MultiTabCompleterBuilder
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender


class Perk(private val plugin: Uwuland) : CommandExecutor {
    init {
        plugin.getCommand("perk")!!.setExecutor(this)
        plugin.getCommand("perk")!!.tabCompleter =
            MultiTabCompleterBuilder()
                .addOnlinePlayerOptions()
                .addStringOptions(mutableListOf("add", "remove"))
                .addFunctionalOptions {
                    val method = it[1]
                    val playerPerks =
                        EventManager.getPerksByName(it[0])
                            .filter { it.type == ListenerType.PERK }
                            .map { it.alias }
                            .toMutableList()

                    when (method) {
                        "add" -> {
                            EventManager.listeners
                                .filter { !playerPerks.contains(it.key) && it.value.type == ListenerType.PERK }.keys
                                .toMutableList()
                        }

                        "remove" -> {
                            playerPerks

                        }

                        else -> {
                            mutableListOf()

                        }
                    }

                }
                .create()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args!!.size < 3) return false
        val p = Bukkit.getPlayer(args[0]) ?: return false
        val listener = EventManager.get(args[2]) ?: return false
        if (listener.type != ListenerType.PERK) return false

        when (args[1]) {
            "add" -> listener.add(p.name, mutableListOf(p.uniqueId))
            "remove" -> listener.remove(p.name)
            else -> return false
        }

        return true
    }
}