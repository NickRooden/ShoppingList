package com.myrood.shoppinglist.domain

data class ShopItem(
    var name: String,
    var count: Int,
    var enabled: Boolean,
    var id: Int = UNDEFINING_ID
)
{
    companion object{
        const val UNDEFINING_ID = -1
    }
}