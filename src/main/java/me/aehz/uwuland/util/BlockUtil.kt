package me.aehz.uwuland.util

import org.bukkit.Location
import org.bukkit.block.Block

object BlockUtil {
    fun getAdjacent(block: Block): MutableList<Block> {
        val blocks = mutableListOf<Block>()

        val x = block.location.x
        val y = block.location.y
        val z = block.location.z

        blocks.add(Location(block.world, x + 1, y, z).block)
        blocks.add(Location(block.world, x - 1, y, z).block)
        blocks.add(Location(block.world, x, y + 1, z).block)
        blocks.add(Location(block.world, x, y - 1, z).block)
        blocks.add(Location(block.world, x, y, z + 1).block)
        blocks.add(Location(block.world, x, y, z - 1).block)

        return blocks
    }

    fun getRadius(block: Block, radius: Double): MutableList<Block> {
        val blocks = mutableListOf<Block>()
        val location = block.location
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

    fun getRadiusSolid(block: Block, radius: Double): MutableList<Block> {
        val blocks = mutableListOf<Block>()
        val location = block.location
        var x = location.x - radius

        while (x <= location.x + radius) {
            var y = location.y - radius
            while (y <= location.y + radius) {
                var z = location.z - radius
                while (z <= location.z + radius) {
                    val b = Location(location.world, x, y, z).block
                    if (b.isSolid) blocks.add(b)
                    z += 1.0
                }
                y += 1.0
            }
            x += 1.0
        }

        return blocks
    }

    fun getBorder(block: Block, radius: Double, height: Double = 1.0): MutableList<Block> {
        val blocks = mutableListOf<Block>()
        val location = block.location

        var x = location.x - radius
        while (x <= location.x + radius) {
            var y = location.y
            while (y <= location.y + height) {
                blocks.add(Location(location.world, x, y, location.z - radius).block)
                blocks.add(Location(location.world, x, y, location.z + radius).block)
                y += 1.0
            }
            x += 1.0
        }

        var z = location.z - radius
        while (z <= location.z + radius) {
            var y = location.y
            while (y <= location.y + height) {
                blocks.add(Location(location.world, location.x - radius, y, z).block)
                blocks.add(Location(location.world, location.x + radius, y, z).block)
                y += 1.0
            }
            z += 1.0
        }
        return blocks
    }

    fun getBorderSolid(block: Block, radius: Double, height: Double = 1.0): MutableList<Block> {
        val blocks = mutableListOf<Block>()
        val location = block.location

        var x = location.x - radius
        while (x <= location.x + radius) {
            var y = location.y
            while (y <= location.y + height) {
                val b1 = Location(location.world, x, y, location.z - radius).block
                val b2 = Location(location.world, x, y, location.z + radius).block
                if (b1.isSolid) blocks.add(b1)
                if (b2.isSolid) blocks.add(b2)
                y += 1.0
            }
            x += 1.0
        }

        var z = location.z - radius
        while (z <= location.z + radius) {
            var y = location.y
            while (y <= location.y + height) {
                val b1 = Location(location.world, location.x - radius, y, z).block
                val b2 = Location(location.world, location.x + radius, y, z).block
                if (b1.isSolid) blocks.add(b1)
                if (b2.isSolid) blocks.add(b2)
                y += 1.0
            }
            z += 1.0
        }
        return blocks
    }
}