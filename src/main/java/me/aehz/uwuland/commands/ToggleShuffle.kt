package me.aehz.uwuland.commands

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.util.MultiTabCompleterBuilder
import me.aehz.uwuland.util.swapEntities
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender


class ToggleShuffle(private val plugin: Uwuland) : CommandExecutor {
    private var taskId: Int = -1

    init {
        plugin.getCommand("toggle_shuffle")!!.setExecutor(this)
        plugin.getCommand("toggle_shuffle")!!.tabCompleter = MultiTabCompleterBuilder()
            .addStringOptions(mutableListOf("enable", "disable"))
            .create()

    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (!sender.isOp) return false
        if (args!!.isEmpty()) return false
        if (args[0] == "enable") {
            startShuffleTask(100)
        } else if (args[0] == "disable") {
            if (taskId == -1) {
                sender.sendMessage("Shuffling is already disabled")
            } else {
                Bukkit.getScheduler().cancelTask(taskId)
                sender.sendMessage("Shuffling has been disabled")
            }
        }
        return true
    }

    private fun startShuffleTask(delay: Long) {
        val random = (13000..23000).random().toLong()
        Bukkit.getLogger().info("NEXT SHUFFLE IN $random")
        taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, Runnable {
            shuffleAllPlayers()
            startShuffleTask(random)
            Bukkit.getLogger().info("SHUFFLED AFTER $delay")
        }, delay)
    }

    fun shuffleAllPlayers() {
        val players = plugin.server.onlinePlayers
        var i = players.size
        while (i > 1) {
            i--
            val random = (0..i - 1).random()
            val player1 = players.elementAt(i)
            val player2 = players.elementAt(random)
            player1.sendMessage("You have been swapped with ${player2.name}")
            player2.sendMessage("You have been swapped with ${player1.name} ")
            swapEntities(player1, player2)
        }
    }
}
