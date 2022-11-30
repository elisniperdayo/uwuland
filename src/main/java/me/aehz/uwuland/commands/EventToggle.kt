package me.aehz.uwuland.commands

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.managers.EventListenerManager
import me.aehz.uwuland.util.MultiTabCompleterBuilder
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender


class EventToggle(private val plugin: Uwuland) : CommandExecutor {
    init {
        plugin.getCommand("event")!!.setExecutor(this)

        val listenerOptions = mutableListOf<String>()
        EventListenerManager.listeners.forEach { listenerOptions.add(it.key) }

        plugin.getCommand("event")!!.tabCompleter =
            MultiTabCompleterBuilder()
                .addOptions(mutableListOf("enable", "disable"))
                .addOptions(listenerOptions)
                .create()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args!!.size < 2) return false
        if (args[0] == "enable") {
            sender.sendMessage("Enabled ${args[1]}")
            EventListenerManager.get(args[1]).enable()
        }
        if (args[0] == "disable") {
            sender.sendMessage("Disabled ${args[1]}")
            EventListenerManager.get(args[1]).disable()
        }
        return true
    }
}