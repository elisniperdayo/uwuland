package me.aehz.uwuland.enums

enum class ApiEventType() {
    DEATH,
    PVP,
    DAMAGE,
    ENCHANT,
    ENDER_EYE,
    JOIN,
    QUIT,
    PORTAL,

    //PERK EVENTS
    SHUFFLE,
    DISORGANIZED_COMMON,
    DISORGANIZED_UNCOMMON,
    DISORGANIZED_RARE,
    DISORGANIZED_SUPER_RARE,
    DISORGANIZED_ONE_IN_A_MILLION,
}

enum class ApiEventOrigin() {
    MINECRAFT, PERK
}
