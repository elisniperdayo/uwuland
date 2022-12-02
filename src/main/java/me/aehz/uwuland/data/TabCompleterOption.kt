package me.aehz.uwuland.data

import me.aehz.uwuland.enums.TabCompleterOptionType

data class TabCompleterOption(
    val type: TabCompleterOptionType,
    val stringOption: MutableList<String> = mutableListOf(),
    val fn: (args: Array<out String>) -> MutableList<String> = fun(_: Array<out String>) = mutableListOf<String>()
)
