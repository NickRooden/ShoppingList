package com.myrood.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.myrood.shoppinglist.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var adapter: ShopListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createRecycler()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.shopList.observe(this){
            adapter.submitList(it)
            }

        val floatButton = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        floatButton.setOnClickListener {
            val intent = ShopItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }


        }
    private fun createRecycler(){
        val r = findViewById<RecyclerView>(R.id.rv_shop_list)
        adapter = ShopListAdapter()
        r.adapter = adapter
        r.recycledViewPool.setMaxRecycledViews(ShopListAdapter.ENABLE, ShopListAdapter.MAX_POOL)
        r.recycledViewPool.setMaxRecycledViews(ShopListAdapter.DISABLE, ShopListAdapter.MAX_POOL)

        adapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
            Log.d("shopitemlongclick","Long click on  $it")

        }


        adapter.onShopItemClickListener = {
            Log.d("shopitemclick","You are victim on click $it")
            val intent = ShopItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)

        }


        //delete shopitem on left/right swipe
        val callBack = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }

        }
        val itemTouchHelper = ItemTouchHelper(callBack)
        itemTouchHelper.attachToRecyclerView(r)

    }




}