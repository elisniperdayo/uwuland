package me.aehz.uwuland.data

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

data class Loot(val material: Material, val amount: IntRange)

object LootTables {

    private val rose = ItemStack(Material.WITHER_ROSE, 1)

    init {
        rose.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5)
        val roseMeta = rose.itemMeta
        roseMeta.displayName(Component.text("One in a million"))
        rose.itemMeta = roseMeta
    }

    val common = arrayOf<Loot>(
        Loot(Material.TORCH, 8..32),
        Loot(Material.BAT_SPAWN_EGG, 1..16),
        Loot(Material.CHARCOAL, 1..12),
        Loot(Material.ARROW, 8..26),
        Loot(Material.BREAD, 6..12),
        Loot(Material.IRON_BARS, 1..16),
        Loot(Material.IRON_ORE, 4..8),
        Loot(Material.IRON_DOOR, 1..4),
        Loot(Material.SPYGLASS, 1..1),
        Loot(Material.PINK_BED, 1..1),
        Loot(Material.COBWEB, 1..5),
        Loot(Material.TRAPPED_CHEST, 1..5),
        Loot(Material.CHAINMAIL_HELMET, 1..1),
        Loot(Material.GRAVEL, 16..64),
        Loot(Material.SAND, 16..52),
        Loot(Material.SNOW, 16..52),
        Loot(Material.MANGROVE_ROOTS, 12..32),
        Loot(Material.GLOWSTONE, 8..24),
        Loot(Material.GRASS_BLOCK, 8..32),
        Loot(Material.COBBLESTONE, 4..24),
        Loot(Material.MYCELIUM, 8..32),
        Loot(Material.MUD_BRICK_STAIRS, 4..24),
        Loot(Material.FLOWER_POT, 1..8),
        Loot(Material.ROTTEN_FLESH, 1..16),
        Loot(Material.BELL, 1..1),
        Loot(Material.SKELETON_SKULL, 1..4),
        Loot(Material.DANDELION, 1..7),
        Loot(Material.CHAIN, 5..21),
        Loot(Material.CLOCK, 1..1),
        Loot(Material.SNOWBALL, 1..1),
        Loot(Material.PAINTING, 1..6),
        Loot(Material.NETHERITE_HOE, 1..1),
        Loot(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, 2..4),
        Loot(Material.BUCKET, 1..3),
        Loot(Material.JUKEBOX, 1..1),

        )

    val uncommon = arrayOf<Loot>(
        Loot(Material.FIREWORK_ROCKET, 4..20),
        Loot(Material.IRON_INGOT, 4..14),
        Loot(Material.CREEPER_SPAWN_EGG, 2..4),
        Loot(Material.STRIPPED_ACACIA_WOOD, 7..15),
        Loot(Material.DIAMOND_BOOTS, 1..1),
        Loot(Material.TNT, 4..16),
        Loot(Material.SLIME_BLOCK, 6..16),
        Loot(Material.HONEY_BLOCK, 6..16),
        Loot(Material.LAVA_BUCKET, 1..1),
        Loot(Material.EXPERIENCE_BOTTLE, 12..36),
        Loot(Material.ANVIL, 1..64),
        Loot(Material.OBSIDIAN, 1..10),
        Loot(Material.STICKY_PISTON, 2..7),
        Loot(Material.SHULKER_SHELL, 1..1),
        Loot(Material.MUSIC_DISC_PIGSTEP, 1..1),
        Loot(Material.MUSIC_DISC_OTHERSIDE, 1..1),
        Loot(Material.GOLD_NUGGET, 14..64),
        Loot(Material.PORKCHOP, 12..28),
        Loot(Material.GOLDEN_CARROT, 1..8),
        Loot(Material.SCULK_SENSOR, 2..6),
        Loot(Material.CHAINMAIL_LEGGINGS, 1..1),

        )

    val rare = arrayOf<Loot>(
        Loot(Material.ENDER_PEARL, 1..4),
        Loot(Material.ENDER_CHEST, 1..3),
        Loot(Material.GOLDEN_APPLE, 2..8),
        Loot(Material.TOTEM_OF_UNDYING, 1..1),
        Loot(Material.BEACON, 1..1),
        Loot(Material.LAPIS_BLOCK, 12..48),
        Loot(Material.WITHER_SKELETON_SPAWN_EGG, 1..4),
        Loot(Material.SHULKER_BOX, 1..1),
        Loot(Material.DIAMOND_BLOCK, 1..3),
        Loot(Material.ENCHANTING_TABLE, 1..1),
        Loot(Material.BEDROCK, 1..1),
    )

    val extraRare = arrayOf<Loot>(
        Loot(Material.ENCHANTED_GOLDEN_APPLE, 1..1),
        Loot(Material.ELYTRA, 1..1),
        Loot(Material.NETHERITE_CHESTPLATE, 1..1),
        Loot(Material.WARDEN_SPAWN_EGG, 1..1),
        Loot(Material.END_CRYSTAL, 2..2),
    )

    val ultraSuperExtraRare = arrayOf<ItemStack>(
        rose
    )
}