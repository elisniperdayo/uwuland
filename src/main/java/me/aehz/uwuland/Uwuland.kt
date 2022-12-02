package me.aehz.uwuland

import me.aehz.uwuland.commands.*
import me.aehz.uwuland.enums.ListenerType
import me.aehz.uwuland.listener.*
import me.aehz.uwuland.listener.global_events.Beta
import me.aehz.uwuland.listener.global_events.ExplosiveArrows
import me.aehz.uwuland.listener.global_events.PlayerJoinGreeting
import me.aehz.uwuland.listener.global_events.RandomFallDamage
import me.aehz.uwuland.listener.group_perks.BindDamage
import me.aehz.uwuland.listener.group_perks.Shuffle
import me.aehz.uwuland.listener.perks.BunnyJump
import me.aehz.uwuland.listener.perks.NarutoSwap
import me.aehz.uwuland.listener.perks.SwapStick
import org.bukkit.WorldCreator
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Uwuland : JavaPlugin() {


    override fun onEnable() {

        File("./worlds.txt").forEachLine { name -> WorldCreator(name).createWorld() }
        //Event listeners
        ProtectDBBlock(this)
        //BINDS NEEDS TO BE STORED IN A DATABASE


        //PEKRS (manage perks inside EventManager)
        NarutoSwap(this, false, ListenerType.PERK)
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
        SwapStick(this, true, ListenerType.PERK)
        BunnyJump(this, true, ListenerType.GROUP_PERK)

        //GROUP EVENTS
        Shuffle(this, true, ListenerType.GROUP_PERK)
        BindDamage(this, true, ListenerType.GROUP_PERK)
        ToggleShuffle(this) // REWORK THIS TO BE TIMED
        // Disorganized: Shuffle inventory on open / on timer

        //GLOBAL EVENTS
        ExplosiveArrows(this, true, false, ListenerType.GLOBAL_EVENT)
        RandomFallDamage(this, true, false, ListenerType.GLOBAL_EVENT)
        PlayerJoinGreeting(this, true, true, ListenerType.GLOBAL_EVENT)
        Beta(this, true, false, ListenerType.GLOBAL_EVENT)

        //Commands
        Beta(this)
        WorldTeleport(this)
        InitializeDBWorld(this)
        EventToggle(this)
        // TODO UPDATE EVENTTOGGLE TO USE FUNCTIONAL TAB COMPLETION
        Settings(this)
        //TODO UPDATE SETTINGS TO USE FUNCTIONAL TAB COMPLETION
        CreateWorld(this)
        Perk(this)
        GroupPerk(this)

        // GROUP EVENTS: /group_event <[Online_players]|[teams]> <add|remove> <event>

        // TIMED EVENTS: Always gets executed after X time.
        // Shuffle example: Store array of [array of players] and shuffle each array


        //IDEAS:
        // FISH FEAR ME (blow up nearby fish)
        // Camera plugin. Register spectator =>
    }

    override fun onDisable() {
    }


}
