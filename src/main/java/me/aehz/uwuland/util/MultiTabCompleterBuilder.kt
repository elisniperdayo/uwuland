package me.aehz.uwuland.util

import me.aehz.uwuland.data.TabCompleterOption
import me.aehz.uwuland.enums.TabCompleterOptionType
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.util.StringUtil

class MultiTabCompleterBuilder() {
    private val optionsList = mutableListOf<TabCompleterOption>()
    private var infinitePlayerOptions = false

    fun addStringOptions(array: MutableList<String>): MultiTabCompleterBuilder {
        optionsList.add(TabCompleterOption(TabCompleterOptionType.STRING_OPTION, stringOption = array))
        return this
    }

    fun addFunctionalOptions(fn: (args: Array<out String>) -> MutableList<String>): MultiTabCompleterBuilder {
        optionsList.add(TabCompleterOption(TabCompleterOptionType.FUNCTIONAL_OPTION, fn = fn))
        return this
    }

    fun addOnlinePlayerOptions(): MultiTabCompleterBuilder {
        addFunctionalOptions {
            Bukkit.getOnlinePlayers().map { it.name }.toMutableList()
        }
        return this
    }

    fun enableInfinitePlayerOptions(): MultiTabCompleterBuilder {
        infinitePlayerOptions = true
        return this
    }

    fun create(): MultiTabCompleter {
        return MultiTabCompleter(optionsList, infinitePlayerOptions)
    }

    class MultiTabCompleter(
        private val optionsList: MutableList<TabCompleterOption>,
        private val infinitePlayerOptions: Boolean = false
    ) : TabCompleter {
        override fun onTabComplete(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<out String>
        ): MutableList<String> {
            val i = args.size - 1

            if (i > optionsList.size - 1 && infinitePlayerOptions) {
                return Bukkit.getOnlinePlayers().map { it.name }.toMutableList()
            } else if (i > optionsList.size - 1) return mutableListOf()

            val options = mutableListOf<String>()
            when (optionsList[i].type) {
                TabCompleterOptionType.STRING_OPTION -> options.addAll(optionsList[i].stringOption)
                TabCompleterOptionType.FUNCTIONAL_OPTION -> options.addAll(optionsList[i].fn(args))
            }

            val completions: MutableList<String> = ArrayList()
            StringUtil.copyPartialMatches(args[i], options, completions)
            completions.sort()

            return completions
        }
    }
}