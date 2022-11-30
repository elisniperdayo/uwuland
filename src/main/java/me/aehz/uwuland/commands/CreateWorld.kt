package me.aehz.uwuland.commands

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.util.MultiTabCompleterBuilder
import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class CreateWorld(private val plugin: Uwuland) : CommandExecutor {
    init {
        plugin.getCommand("new_world")!!.setExecutor(this)
        plugin.getCommand("new_world")!!.tabCompleter =
            MultiTabCompleterBuilder().create()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args!!.size < 1) return false
        if (sender is Player) return false
        val creator = WorldCreator(args[0])
        Bukkit.getServer().createWorld(creator)
        return true
    }
}