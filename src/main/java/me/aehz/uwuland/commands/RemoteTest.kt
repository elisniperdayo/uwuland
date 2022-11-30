package me.aehz.uwuland.commands

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.util.MultiTabCompleterBuilder
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender


class RemoteTest(private val plugin: Uwuland) : CommandExecutor {
    init {
        plugin.getCommand("remote")!!.setExecutor(this)
        plugin.getCommand("remote")!!.tabCompleter =
            MultiTabCompleterBuilder().create()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "uwu")
        return true
    }
}