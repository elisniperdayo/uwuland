@file:JvmName("Utils")
@file:JvmMultifileClass

package me.aehz.uwuland.util

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

fun getRadius(location: Location, r: Double): MutableList<Block> {
    val blocks = mutableListOf<Block>()

    var x = location.x - r

    while (x <= location.x + r) {
        var y = location.y - r
        while (y <= location.y + r) {
            var z = location.z - r
            while (z <= location.z + r) {
                blocks.add(Location(location.world, x, y, z).block)
                z += 1.0
            }
            y += 1.0
        }
        x += 1.0
    }

    return blocks
}