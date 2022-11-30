package me.aehz.uwuland.commands

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.util.MultiTabCompleterBuilder
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender


class Uwu(private val plugin: Uwuland) : CommandExecutor {
    init {
        plugin.getCommand("uwu")!!.setExecutor(this)
        plugin.getCommand("uwu")!!.tabCompleter = MultiTabCompleterBuilder()
            .addOptions(mutableListOf("a", "b")).create()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        val saofan = Bukkit.getServer().getPlayer("SAOFAN")
        saofan!!.damage(0.0001)
        return true
    }
}

