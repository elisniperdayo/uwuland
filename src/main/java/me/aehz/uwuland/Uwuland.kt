package me.aehz.uwuland

import me.aehz.uwuland.commands.*
import me.aehz.uwuland.listener.*
import me.aehz.uwuland.listener.global_events.*
import me.aehz.uwuland.listener.global_events.Beta
import me.aehz.uwuland.listener.group_perks.BindDamage
import me.aehz.uwuland.listener.group_perks.Shuffle
import me.aehz.uwuland.listener.perks.*
import org.bukkit.WorldCreator
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

object PluginInstance {
    var instance: Uwuland? = null
    fun get(): Uwuland? {
        return instance
    }

    fun set(plugin: Uwuland) {
        instance = plugin
    }
}

class Uwuland : JavaPlugin() {


    override fun onEnable() {

        PluginInstance.set(this)

        File("./worlds.txt").forEachLine { name -> WorldCreator(name).createWorld() }
        //Event listeners
        ProtectDBBlock(this)
        //BINDS NEEDS TO BE STORED IN A DATABASE


        //PEKRS (manage perks inside EventManager)
        NarutoSwap()
        Tesla()
        Firestarter()
        Xray()
        Speedrunner()
        Naruto()

        // TODO ADD Speedrunner infinite glass, slowly refilling
        // TODO MAKE TIMED TASKS NOT CRASH IF PLAYERS LEFT
        // TODO CHECK Shuffle
        // TODO MAKE RandomFallDamage a solo perk. Also make it able to heal
        // TODO ADD SETTINGS OBJECT TO PERKLISTENERS

        // ShortSighted: Unable to see Entities outside a 4 block radius. Unable to be seen by other Entities outside a 4 block radius
        // Abduction: Swap stick but on every left click (outside cqc. 7 second cooldown against (DONT SWAP INV )
        // Disorganized: Shuffle inventory on open / on timer, keeps hotbar on death
        // Photosynthesis: Gain buffs / debuffs based on light level (reapply every second )
        // Magnet: Attract everything (less range / effect on players)
        // Short fuse: explode every 30? seconds. Immune to explosion damage (dont destroy chests if possible)
        // Phoenix: On death => slowly revive and burn area. Turn water into lava, lower max hp
        // Bunny: Takes damage when alone. Implement BunnyJump. (No fall damage?) DONT LET ENDERDRAGON BE BUNNIFIED
        // FISH FEAR ME (blow up nearby fish)
        SwapStick()
        BunnyJump()

        //GROUP EVENTS
        Shuffle()
        BindDamage()

        //GLOBAL EVENTS
        ExplosiveArrows()
        RandomFallDamage()
        PlayerJoinGreeting()
        Beta()

        //Commands
        me.aehz.uwuland.commands.Beta(this)
        WorldTeleport(this)
        //InitializeDBWorld(this)
        EventToggle(this)
        Settings(this)
        //TODO UPDATE SETTINGS TO USE FUNCTIONAL TAB COMPLETION
        //CreateWorld(this)
        Perk(this)
        GroupPerk(this)
        //ToggleShuffle(this) // REWORK THIS TO BE TIMED

        // GROUP EVENTS: /group_event <[Online_players]|[teams]> <add|remove> <event>

        // TIMED EVENTS: Always gets executed after X time.
        // Shuffle example: Store array of [array of players] and shuffle each array


        //IDEAS:
        // Camera plugin. Register spectator =>
    }

    override fun onDisable() {
    }


}
