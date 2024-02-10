package me.aehz.uwuland.util

import org.bukkit.World
import org.bukkit.entity.Entity

object EntityUtil {
    fun isInNether(e: Entity): Boolean {
        return e.world.environment == World.Environment.NETHER
    }

    fun isInEnd(e: Entity): Boolean {
        return e.world.environment == World.Environment.THE_END
    }
}