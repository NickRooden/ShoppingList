package com.myrood.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.myrood.shoppinglist.R
import com.myrood.shoppinglist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bind: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    private lateinit var adapterSL: ShopListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        createRecycler()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.shopList.observe(this){
            adapterSL.submitList(it)
            }

        bind.buttonAddShopItem.setOnClickListener {

            if (isLandOrient()){

                launchFragment(ShopItemFragment.newInstAdd())

            }else{
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            }
        }
    }

    private fun isLandOrient() = bind.fragLandContainer != null

    private fun launchFragment(fragment: ShopItemFragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frag_land_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun createRecycler(){

        adapterSL = ShopListAdapter()
        with(bind.rvShopList){
            adapter = adapterSL
            recycledViewPool.setMaxRecycledViews(ShopListAdapter.ENABLE, ShopListAdapter.MAX_POOL)
            recycledViewPool.setMaxRecycledViews(ShopListAdapter.DISABLE, ShopListAdapter.MAX_POOL)
        }

        adapterSL.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
            Log.d("shopitemlongclick","Long click on  $it")

        }

        adapterSL.onShopItemClickListener = {

            if (isLandOrient()){
                launchFragment(ShopItemFragment.newInstEdit(it.id))
            }else {
                Log.d("shopitemclick", "You are victim for click $it")
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            }
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
                val item = adapterSL.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }

        }
        val itemTouchHelper = ItemTouchHelper(callBack)
        itemTouchHelper.attachToRecyclerView(bind.rvShopList)

    }
}