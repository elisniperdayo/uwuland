package me.aehz.uwuland

import me.aehz.uwuland.commands.*
import me.aehz.uwuland.listener.*
import org.bukkit.WorldCreator
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Uwuland : JavaPlugin() {


    override fun onEnable() {

        File("./worlds.txt").forEachLine { name -> WorldCreator(name).createWorld() }
        //Event listeners
        Beta(this, false)
        ProtectDBBlock(this)
        PlayerJoinGreeting(this, true)
        //BINDS NEEDS TO BE STORED IN A DATABASE


        //PLAYER EVENTS (manage perks inside of EventManager)
        NarutoSwap(this, false)
        // ?: Thunder all nearby entities. (item or class??)
        // Drunk Spectre: Upside down. Flying. Slowed.
        // Firestarter: Burn all wood in area. Gets fire resistance
        // Jester. Get a random perk every 5 minutes.
        // Now you see me, now I don't: Unable to see Entities outside a 4 block radius. Unable to be seen by other Entities outside a 4 block radius
        // Speedrunner: Can no longer crouch (Maybe just damage when below certain speed?). Blocks don't decrease when building (except for obsidian / ores). ()
        // Photosynthesis: Gain buffs / debuffs based on light level (reapply every second )
        // Abduction: Swap stick but on every left click (outside cqc. 7 second cooldown against players via timestamp)
        // Magnet: Attract everything (less range / effect on players)
        // Short fuse: explode every 30? seconds. Immune to explosion damage (dont destroy chests if possible)
        // Phoenix: On death => slowly
        // Bunny: Takes damage when alone. Implement BunnyJump. (No fall damage?) DONT LET ENDERDRAGON BE BUNNIFIED

        //GROUP EVENTS
        BindDamage(this, true)
        ToggleShuffle(this) // REWORK THIS TO BE TIMED
        BunnyJump(this, true)
        // Disorganized: Shuffle inventory on open /

        //GLOBAL EVENTS
        SwapStick(this, true)
        ExplosiveArrows(this, true)
        RandomFallDamage(this, true)


        //Commands
        Uwu(this)
        WorldTeleport(this)
        InitializeDBWorld(this)
        RemoteTest(this)
        EventToggle(this)
        Settings(this)
        CreateWorld(this)


        // PLAYER EVENTS (PERK): Affect one entity.
        // GROUP EVENTS: Affect a group of entities
        // WORLD EVENTS: Affect every entity

        // GROUP EVENTS: /group_event <global|[teams]> <add|remove> <event>
        //

        // TIMED EVENTS: Always gets executed after X time.
        // Shuffle example: Store array of [array of players] and shuffle each array


        //IDEAS:
        // FISH FEAR ME (blow up nearby fish)
        // Camera plugin. Register spectator =>
    }

    override fun onDisable() {
    }
}
