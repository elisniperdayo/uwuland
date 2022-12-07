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