package me.aehz.uwuland.commands

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.util.MultiTabCompleterBuilder
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class WorldTeleport(private val plugin: Uwuland) : CommandExecutor {
    private val worldNames = mutableListOf<String>()

    init {
        plugin.getCommand("world")!!.setExecutor(this)
        Bukkit.getWorlds().forEach { world -> worldNames.add(world.name) }
        plugin.getCommand("world")!!.tabCompleter =
            MultiTabCompleterBuilder().addStringOptions(worldNames).create()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return true
        val worldName = args!![0]
        if (!worldNames.contains(worldName)) return true
        val world = Bukkit.getWorld(worldName)
        sender.teleport(world!!.spawnLocation)
        return true
    }
}
