package me.aehz.uwuland.commands

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.util.MultiTabCompleterBuilder
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class Settings(private val plugin: Uwuland) : CommandExecutor {
    init {
        plugin.getCommand("settings")!!.setExecutor(this)

        plugin.getCommand("settings")!!.tabCompleter =
            MultiTabCompleterBuilder()
                .addStringOptions(EventManager.listeners.keys.toMutableList())
                .addFunctionalOptions {
                    val listener = EventManager.get(it[0])
                    listener?.getSettings() ?: mutableListOf()
                }
                .addFunctionalOptions {
                    val listener = EventManager.get(it[0])
                    val field = listener!!::class.java.getDeclaredField("SETTING_${it[1]}")
                    field.trySetAccessible()
                    val currentValue = field.get(listener)
                    mutableListOf("$currentValue")
                }
                .create()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size < 3) return false
        val listener = EventManager.get(args[0]) ?: return false
        val stringValue = args[2]
        val fieldName = args[1]
        val field = listener::class.java.getDeclaredField("SETTING_$fieldName")
        field.trySetAccessible()
        val fieldValue = field.get(listener)

        val value = stringToSameType(stringValue, fieldValue)
        if (value == null) {
            sender.sendMessage("§cType invalid conversion to ${fieldValue::class} .")
            return false
        }
        val set = listener.setSettings(fieldName, value)
        if (!set) {
            sender.sendMessage("§cFailed to change setting.")
        } else {
            sender.sendMessage("Successfully set $fieldName of ${listener.alias} to $value")
        }
        return true
    }

    private fun stringToSameType(string: String, typeValue: Any): Any? {
        return when (typeValue) {
            is Int -> string.toIntOrNull()
            is Double -> string.toDoubleOrNull()
            is Long -> string.toLongOrNull()
            is Float -> string.toFloatOrNull()
            is Boolean -> string.toBooleanStrictOrNull()
            is IntRange -> stringToIntRange(string)
            is String -> string
            else -> null
        }
    }

    private fun stringToIntRange(rangeString: String): IntRange {
        val (start, end) = rangeString.split("..").map { it.toInt() }
        return start..end
    }
}