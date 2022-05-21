package com.myrood.shoppinglist.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myrood.shoppinglist.domain.ShopItem
import com.myrood.shoppinglist.domain.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl: ShopListRepository {

    private var shopListLD = MutableLiveData<List<ShopItem>>()
    private var shopList = sortedSetOf<ShopItem>({o1,o2 -> o1.id.compareTo(o2.id)})

    private var autoInkrement = 0

    init {
        for (i in 0 until 20){
            var item = ShopItem("Name-$i",i, Random.nextBoolean())
            addShopItem(item)
        }
    }


    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Where is it shopItem for getting...")
    }

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINING_ID){
            shopItem.id = autoInkrement++
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldItem = getShopItem(shopItem.id)

        deleteShopItem(oldItem)
        addShopItem(shopItem)
        Log.d("fun editShopItem","fun editShopItem $shopItem")
    }
    private fun updateList(){
        shopListLD.value = shopList.toList()
    }


}