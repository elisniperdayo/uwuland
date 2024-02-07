package me.aehz.uwuland.commands

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.abstracts.PerkListener
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.util.MultiTabCompleterBuilder
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class Settings(private val plugin: Uwuland) : CommandExecutor {
    init {
        plugin.getCommand("settings")!!.setExecutor(this)

        plugin.getCommand("settings")!!.tabCompleter =
            MultiTabCompleterBuilder()
                .addStringOptions(EventManager.listeners.filter { it.value.stg.isNotEmpty() }.keys.toMutableList())
                .addStringOptions(mutableListOf("set", "get"))
                .addFunctionalOptions {
                    when (it[1]) {
                        "set" -> {
                            val listener = EventManager.get(it[0])
                            listener?.getSettings() ?: mutableListOf()
                        }

                        else -> mutableListOf()
                    }
                }
                .addFunctionalOptions {
                    when (it[1]) {
                        "set" -> mutableListOf("§7<Value>")
                        else -> mutableListOf()
                    }
                }
                .create()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args!!.size < 2) return false
        val listener = EventManager.get(args[0]) ?: return false
        val method = args[1]
        if (method == "get") {
            sender.sendMessage("Available settings for ${args[0]}:\n${getFormattedAvailableSettings(listener)}")
        }
        if (method == "set" && args.size == 4) {
            val key = args[2]
            val value = args[3]
            val set = listener.setStg(key, value)
            if (!set) {
                sender.sendMessage(
                    "§cSetting does not exist. Available settings for ${listener.alias}:\n${
                        getFormattedAvailableSettings(
                            listener
                        )
                    }"
                )
            } else {
                sender.sendMessage("Successfully set ${key} of ${listener.alias} to ${value}")
            }
        }
        return true
    }

    private fun getFormattedAvailableSettings(listener: PerkListener): String {
        var availableSettings = ""
        listener.stg.forEach {
            availableSettings += "\n${it.key}: ${it.value}"
        }
        return availableSettings
    }
}