package me.aehz.uwuland.listener

import me.aehz.uwuland.abstracts.PerkListener
import me.aehz.uwuland.enums.PerkSettingConversion

class PerkSetting<T>(
    private var value: T,
    private val parent: PerkListener,
    val conversion: PerkSettingConversion?,
) {

    fun get(): T {
        return when (conversion) {
            PerkSettingConversion.SECONDS_TO_TICKS -> {
                return safeMultiplication(value, 20.0)
            }

            PerkSettingConversion.MINUTES_TO_TICKS -> {
                return safeMultiplication(value, 1200.0)
            }

            else -> value
        }
    }

    fun set(v: T) {
        value = v
    }

    private fun safeMultiplication(v: T, multiplyBy: Double): T {
        return when {
            v is Number -> (v.toDouble() * multiplyBy).toInt() as T
            v is IntRange -> ((v.first * 20)..(v.last * 20)) as T
            else -> v
        }
    }
}