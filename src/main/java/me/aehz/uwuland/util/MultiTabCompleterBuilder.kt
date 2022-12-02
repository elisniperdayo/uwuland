package me.aehz.uwuland.util

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.util.StringUtil

class MultiTabCompleterBuilder() {
    private val options = mutableListOf<MutableList<String>>()

    fun addStringOptions(array: MutableList<String>): MultiTabCompleterBuilder {
        this.options.add(array)
        return this
    }

    fun create(): MultiTabCompleter {
        return MultiTabCompleter(options)
    }

    class MultiTabCompleter(private val options: MutableList<MutableList<String>>) : TabCompleter {
        override fun onTabComplete(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<out String>?
        ): MutableList<String>? {
            val i = args!!.size - 1
            if (i > options.size - 1) return mutableListOf()

            val completions: MutableList<String> = ArrayList()
            StringUtil.copyPartialMatches(args!![i], options[i], completions)
            completions.sort()

            return completions
        }
    }
}

