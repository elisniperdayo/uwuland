package me.aehz.uwuland.commands

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.util.MultiTabCompleterBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.generator.ChunkGenerator
import java.util.Random


class InitializeDBWorld(private val plugin: Uwuland) : CommandExecutor {
    init {
        plugin.getCommand("initialize_database_world")!!.setExecutor(this)
        plugin.getCommand("initialize_database_world")!!.tabCompleter =
            MultiTabCompleterBuilder().create()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is Player) return true
        if (Bukkit.getWorld("DatabaseWorld847593474398") == null) {
            val creator = WorldCreator("DatabaseWorld847593474398")
            creator.generator(EmptyChunkGenerator())
            creator.createWorld()
            val DBBlock = Bukkit.getWorld("DatabaseWorld847593474398")!!.getBlockAt(0, 123, 0)
            DBBlock.type = Material.BEDROCK
        }

        return true
    }
}

class EmptyChunkGenerator : ChunkGenerator() {
    override fun generateChunkData(world: World, random: Random, x: Int, z: Int, biome: BiomeGrid): ChunkData {
        return createChunkData(world)
    }
}