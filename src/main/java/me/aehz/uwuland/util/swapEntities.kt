@file:JvmName("Utils")
@file:JvmMultifileClass

package me.aehz.uwuland.util

import org.bukkit.entity.Entity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

fun swapEntities(e1: Entity, e2: Entity) {
    if (!(e1 !is Mob || e1 !is Player) || !(e2 !is Mob || e2 !is Player)) return
    val loc1 = e1.location
    val loc2 = e2.location

    if (e1 is Player && e2 is Player) {
        val inv1 = e1.inventory.contents
        val inv2 = e2.inventory.contents
        e1.inventory.contents = inv2
        e2.inventory.contents = inv1

        e1.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 40, 20))
        e1.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 30, 3))
        e1.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 30))

        e2.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 40, 20))
        e2.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 30, 3))
        e2.addPotionEffect(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 30))

    }
    e1.teleport(loc2)
    e2.teleport(loc1)
}