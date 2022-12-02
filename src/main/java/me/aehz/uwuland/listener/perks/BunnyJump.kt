package me.aehz.uwuland.listener.perks

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent
import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.data.PerkOwner
import me.aehz.uwuland.interfaces.PerkListener
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.enums.ListenerType
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class BunnyJump(
    override val plugin: Uwuland,
    override var isEnabled: Boolean,
    override val type: ListenerType,
    override var perkOwners: MutableList<PerkOwner>
) : PerkListener {
    override var stg = mutableMapOf<String, String>()

    init {
        stg["power"] = "5"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)
    }

    private val crouch = HashMap<String, HashMap<String, Float>>()

    @EventHandler
    fun onCrouch(event: PlayerToggleSneakEvent) {
        if (!isEnabled || !hasPerk(event.player)) return
        val p = event.player
        val s = Bukkit.getServer().scheduler

        if (event.isSneaking) {
            crouch[p.name] = HashMap<String, Float>()
            crouch[p.name]!!["prevExp"] = p.exp
            crouch[p.name]!!["prevLevel"] = p.level.toFloat()
            crouch[p.name]!!["pickedUpExp"] = 0f
            p.exp = 0f
            p.level = 0

            crouch[p.name]!!["taskId"] = s.runTaskTimer(plugin, Runnable {
                if (p.exp < 0.8f) {
                    p.giveExp(1)
                } else {
                    p.exp = 1.0f
                }
            }, 4, 8).taskId.toFloat()
        } else {
            val power = stg["power"]!!.toFloat()
            val boost = (power * p.exp).toInt()
            p.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 17, boost))
            if (p.exp == 1.0f) {
                for (entity in p.location.chunk.entities) {
                    if (entity !is Player && entity is Mob) {
                        val loc = entity.location
                        val rabbit = p.world.spawnEntity(loc, EntityType.RABBIT)
                        entity.remove()
                    }
                }
            }

            p.exp = crouch[p.name]!!["prevExp"]!!
            p.level = crouch[p.name]!!["prevLevel"]!!.toInt()
            p.giveExp(crouch[p.name]!!["pickedUpExp"]!!.toInt())

            s.cancelTask(crouch[p.name]!!["taskId"]!!.toInt())
            crouch.remove(p.name)
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onExp(e: PlayerPickupExperienceEvent) {
        if (!isEnabled) return
        val p = e.player
        if (crouch.containsKey(p.name)) {
            crouch[p.name]!!["pickedUpExp"] = crouch[p.name]!!["pickedUpExp"]!! + e.experienceOrb.experience.toFloat()
            e.experienceOrb.remove()
            e.isCancelled = true
        }
    }
}