package com.myrood.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myrood.shoppinglist.R
import com.myrood.shoppinglist.databinding.ItemShopEnableBinding
import com.myrood.shoppinglist.databinding.ItemShopDisaibleBinding
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

    class ItemViewHolder(
        val bind: ViewDataBinding
        ) : RecyclerView.ViewHolder(bind.root){

        //val tvname = itemView.findViewById<TextView>(R.id.tv_name)
        //val tvcount = itemView.findViewById<TextView>(R.id.tv_count)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        Log.d("onCreateViewHolder", "${count++}")


        val layoutType = when(viewType){
            ENABLE -> R.layout.item_shop_enable
            DISABLE -> R.layout.item_shop_disaible
            else -> throw RuntimeException ("Wrong viewType: Int $viewType")
        }
        val bind = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layoutType,
            parent,
            false
        )
        return ItemViewHolder(bind)
    }

    override fun onBindViewHolder(viewBindHolder: ItemViewHolder, position: Int) {

        // Somewhere here from that ListAdapter we take getItem, our shopList
        val shopItem = getItem(position)

        viewBindHolder.bind.root.setOnLongClickListener {

            onShopItemLongClickListener?.invoke(shopItem)

            true
        }

        viewBindHolder.bind.root.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
        when(viewBindHolder.bind){
            is ItemShopEnableBinding -> {
                viewBindHolder.bind.tvName.text = shopItem.name
                viewBindHolder.bind.tvCount.text = shopItem.count.toString()
            }
            is ItemShopDisaibleBinding -> {
                viewBindHolder.bind.tvName.text = shopItem.name
                viewBindHolder.bind.tvCount.text = shopItem.count.toString()
            }
        }
        

    }


    override fun getItemViewType(position: Int): Int {

        val sh = getItem(position)
        return if (sh.enabled) ENABLE else DISABLE

    }


}