package me.aehz.uwuland.API

import me.aehz.uwuland.API.Data.*
import me.aehz.uwuland.PluginInstance
import me.aehz.uwuland.enums.ApiEventType
import me.aehz.uwuland.managers.ApiEventManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EnderDragon
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Tameable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerQuitEvent

class ApiEventListener : Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, PluginInstance.get()!!)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamage(e: EntityDamageEvent) {
        val isTamed = e.entity is Tameable && (e.entity as Tameable).isTamed
        if (e.entity !is Player && !isTamed) return

        val data = ApiDataEvent.Minecraft.Damage(
            ApiDataConverter.entity(e.entity),
            e.finalDamage,
            e.cause.name
        )
        ApiEventManager.add(data)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityDamage(e: EntityDamageByEntityEvent) {
        if (e.entity !is Player || e.damager !is Player) return
        val data = ApiDataEvent.Minecraft.Pvp(
            ApiDataConverter.entity(e.entity),
            ApiDataConverter.entity(e.damager),
            e.finalDamage,
            e.cause.name,
        )
        ApiEventManager.add(data)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDeath(e: EntityDeathEvent) {
        val isTamed = e.entity is Tameable && (e.entity as Tameable).isTamed
        if (e.entity !is Player && !isTamed) return
        val data = ApiDataEvent.Minecraft.Death(
            ApiDataConverter.entity(e.entity),
        )
        ApiEventManager.add(data)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoin(e: PlayerJoinEvent) {
        val data = ApiDataEvent.Minecraft.Join(
            ApiDataConverter.entity(e.player),
            e.player.ping
        )
        ApiEventManager.add(data)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onQuit(e: PlayerQuitEvent) {
        val data = ApiDataEvent.Minecraft.Quit(
            ApiDataConverter.entity(e.player),
            e.player.ping
        )
        ApiEventManager.add(data)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPortal(e: PlayerPortalEvent) {
        val data = ApiDataEvent.Minecraft.Portal(
            ApiDataConverter.entity(e.player),
            ApiDataConverter.location(e.from),
            ApiDataConverter.location(e.to)
        )
        ApiEventManager.add(data)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEnchant(e: EnchantItemEvent) {
        e.enchantsToAdd.map { it.key.displayName(1) }
        val data = ApiDataEvent.Minecraft.Enchant(
            ApiDataConverter.entity(e.enchanter),
            ApiDataConverter.itemStack(e.item),
            ApiDataConverter.enchantmentMapToList((e.enchantsToAdd)),
        )
        ApiEventManager.add(data)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInteract(e: PlayerInteractEvent) {
        if (e.action.isRightClick && e.item?.type == Material.ENDER_EYE) {
            val data = ApiDataEvent.Minecraft.EnderEye(
                ApiDataConverter.entity(e.player),
            )
            ApiEventManager.add(data)
        }
    }
}