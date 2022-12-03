package me.aehz.uwuland.commands

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.util.MultiTabCompleterBuilder
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender


class EventToggle(private val plugin: Uwuland) : CommandExecutor {
    init {
        plugin.getCommand("event")!!.setExecutor(this)

        plugin.getCommand("event")!!.tabCompleter =
            MultiTabCompleterBuilder()
                .addStringOptions(mutableListOf("enable", "disable"))
                .addFunctionalOptions {
                    when (it[0]) {
                        "enable" -> EventManager.listeners.filter { !it.value.isEnabled }.keys.toMutableList()
                        "disable" -> EventManager.listeners.filter { it.value.isEnabled }.keys.toMutableList()
                        else -> mutableListOf()
                    }

                }
                .create()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args!!.size < 2) return false
        val listener = EventManager.get(args[1]) ?: return false

        when (args[0]) {
            "enable" -> {
                sender.sendMessage("Enabled ${args[1]}")
                listener.enable()
            }

            "disable" -> {
                sender.sendMessage("Disabled ${args[1]}")
                listener.disable()
            }

            else -> return false
        }

        return true
    }
}