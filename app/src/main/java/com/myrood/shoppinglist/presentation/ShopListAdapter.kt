package com.myrood.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myrood.shoppinglist.R
import com.myrood.shoppinglist.domain.ShopItem

class ShopListAdapter :
    ListAdapter<ShopItem, ShopListAdapter.ItemViewHolder>(ShopItemDiffCallBack()){

    companion object{
        const val ENABLE = 1
        const val DISABLE = 0

        const val MAX_POOL = 7
    }

    var count = 1

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val tvname = itemView.findViewById<TextView>(R.id.tv_name)
        val tvcount = itemView.findViewById<TextView>(R.id.tv_count)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        Log.d("onCreateViewHolder", "${count++}")


        val layoutType = when(viewType){
            ENABLE -> R.layout.item_shop_enable
            DISABLE -> R.layout.item_shop_disaible
            else -> throw RuntimeException ("Wrong viewType: Int $viewType")
        }

        val view = LayoutInflater.from(
            parent.context).inflate(layoutType, parent, false
        )

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {

        // Somewhere here from that ListAdapter we take getItem, our shopList
        val shopItem = getItem(position)

        viewHolder.itemView.setOnLongClickListener {

            onShopItemLongClickListener?.invoke(shopItem)

            true
        }

        viewHolder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
        
        viewHolder.tvname.text = shopItem.name
        viewHolder.tvcount.text = shopItem.count.toString()
    }


    override fun getItemViewType(position: Int): Int {

        val sh = getItem(position)
        return if (sh.enabled) ENABLE else DISABLE

    }


}