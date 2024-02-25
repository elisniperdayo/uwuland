package me.aehz.uwuland.API.Data

data class ApiDataItemStack(
    val name: String,
    val amount: Int,
    val enchantments: List<ApiDataEnchantment>,
)