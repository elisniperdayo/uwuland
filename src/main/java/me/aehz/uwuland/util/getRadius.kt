@file:JvmName("Utils")
@file:JvmMultifileClass

package me.aehz.uwuland.util

import org.bukkit.Location
import org.bukkit.block.Block


fun getRadius(location: Location, radius: Double): MutableList<Block> {
    val blocks = mutableListOf<Block>()

    var x = location.x - radius

    while (x <= location.x + radius) {
        var y = location.y - radius
        while (y <= location.y + radius) {
            var z = location.z - radius
            while (z <= location.z + radius) {
                blocks.add(Location(location.world, x, y, z).block)
                z += 1.0
            }
            y += 1.0
        }
        x += 1.0
    }

    return blocks
}