package me.aehz.uwuland.commands

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.util.CustomListener
import me.aehz.uwuland.util.EventListenerManager
import me.aehz.uwuland.util.MultiTabCompleterBuilder
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class Settings(private val plugin: Uwuland) : CommandExecutor {
    init {
        plugin.getCommand("settings")!!.setExecutor(this)

        val listenerOptions = mutableListOf<String>()
        EventListenerManager.listeners.forEach { listenerOptions.add(it.key) }

        plugin.getCommand("settings")!!.tabCompleter =
            MultiTabCompleterBuilder()
                .addOptions(listenerOptions)
                .addOptions(mutableListOf("set", "get"))
                .addOptions(mutableListOf("_Use *get* to see a list of available settings"))
                .addOptions(mutableListOf("_Use *get* to see a list of available settings"))
                .create()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args!!.size < 2) return false
        val listener = EventListenerManager.get(args[0])
        val method = args[1]
        if (method == "get") {
            sender.sendMessage("Available settings for ${args[0]}:\n${getFormattedAvailableSettings(listener)}")
        }
        if (method == "set" && args!!.size == 4) {
            val key = args[2]
            val value = args[3]
            val set = listener.setStg(key, value)
            if (!set) {
                sender.sendMessage(
                    "§cSetting does not exist. Available settings for ${args[0]}:\n${
                        getFormattedAvailableSettings(
                            listener
                        )
                    }"
                )
            } else {
                sender.sendMessage("Successfully set ${key} of ${args[0]} to ${value}")
            }
        }
        return true
    }

    private fun getFormattedAvailableSettings(listener: CustomListener): String {
        var availableSettings = ""
        listener.stg.forEach {
            availableSettings += "\n${it.key}: ${it.value}"
        }
        return availableSettings
    }
}