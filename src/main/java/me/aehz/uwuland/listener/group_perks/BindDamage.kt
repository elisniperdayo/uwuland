package me.aehz.uwuland.listener.group_perks

import io.papermc.paper.event.player.PlayerArmSwingEvent
import org.bukkit.entity.Player

import me.aehz.uwuland.Uwuland
import me.aehz.uwuland.data.PerkOwner
import org.bukkit.Bukkit
import me.aehz.uwuland.interfaces.PerkListener
import me.aehz.uwuland.managers.EventManager
import me.aehz.uwuland.enums.ListenerType
import org.bukkit.EntityEffect
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent


class BindDamage(
    private val plugin: Uwuland,
    override var isEnabled: Boolean,
    override val type: ListenerType,
    override var perkOwners: MutableList<PerkOwner>
) :
    PerkListener {
    override var stg = mutableMapOf<String, String>()

    init {
        stg["damageMultiplier"] = "1"
        Bukkit.getPluginManager().registerEvents(this, plugin)
        EventManager.register(this, type)
    }

    val binds = mutableMapOf<Entity, MutableList<Entity>>()

    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        if (!isEnabled) return
        val dmg = e.finalDamage * stg["damageMultiplier"]!!.toFloat()
        val damageTaker = e.entity
        if (e.damage == 0.0) return
        if (!binds.containsKey(damageTaker)) return
        for (partner in binds[damageTaker]!!) {
            if (partner is LivingEntity && damageTaker is LivingEntity) {
                partner.playEffect(EntityEffect.HURT)

                if (partner.health - dmg < 0.0) {
                    partner.health = 0.0
                } else {
                    partner.health -= dmg
                }

                //Unbind if partner was a mob. DamageTaker damage is not calculated at this point
                if ((partner !is Player && partner.isDead) || (damageTaker !is Player && damageTaker.health <= dmg)) {
                    unbind(damageTaker, partner)
                    unbind(partner, damageTaker)
                }
            }
        }
    }

    @EventHandler
    fun onReg(e: EntityRegainHealthEvent) {
        if (!isEnabled) return
        val healTaker = e.entity
        if (!binds.containsKey(healTaker)) return
        for (partner in binds[healTaker]!!) {
            if (!(partner is LivingEntity && healTaker is LivingEntity)) return
            if (partner.health <= 0) return
            val partnerMaxHealth = partner.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
            if (partnerMaxHealth < partner.health + e.amount) {
                partner.health = partnerMaxHealth
            } else {
                partner.health += e.amount
            }
        }
    }

    @EventHandler
    fun onHit(e: PlayerArmSwingEvent) {
        val p = e.player
        val target = e.player.getTargetEntity(100)
        if (target !is LivingEntity) return

        if (p.inventory.itemInMainHand.type == Material.BEDROCK) {
            for (p in Bukkit.getServer().onlinePlayers) {
                for (mod in p.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.modifiers) {
                    p.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.removeModifier(mod)
                }
            }
            Bukkit.broadcastMessage("Cleared all bindings")
            binds.clear()
        }

        if (p.inventory.itemInMainHand.type != Material.OBSIDIAN || target !is LivingEntity) return
        bindList(mutableListOf(p, target))
    }

    private fun bindList(list: MutableList<Entity>) {
        for (e1 in list) {
            for (e2 in list) {
                if (e1 != e2) {
                    bindEntities(e1, e2)
                }
            }
        }
    }

    private fun bindEntities(e1: Entity, e2: Entity) {
        if (binds.containsKey(e1)) {
            binds[e1]!!.add(e2)
        } else {
            binds[e1] = mutableListOf(e2)
        }

        if (e1 is LivingEntity && e2 is LivingEntity) {
            val e2HP = e2.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue
            e1.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.addModifier(
                AttributeModifier(
                    e2.uniqueId,
                    "bind hp modifier",
                    e2HP,
                    AttributeModifier.Operation.ADD_NUMBER
                )
            )
        }
    }

    fun unbind(e1: Entity, e2: Entity) {
        if (!binds.containsKey(e1)) return
        if (binds[e1]!!.size == 1) {
            binds.remove(e1)
        } else {
            binds[e1]!!.remove(e2)
        }
        if (e1 is LivingEntity && e2 is LivingEntity) {
            val modifier =
                e1.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.modifiers.filter { it.uniqueId == e2.uniqueId }[0]
            e1.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.removeModifier(modifier)
        }
    }
}
