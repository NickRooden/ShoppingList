package com.myrood.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.myrood.shoppinglist.R
import com.myrood.shoppinglist.databinding.ActivityShopItemBinding

class ShopItemActivity : AppCompatActivity() {


    private lateinit var bind: ActivityShopItemBinding

    private var screenmode = MODE_EMPTY
    private var shopItemId = SHOP_ITEM_ID_EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(bind.root)

        parseIntent()

        //to stop create fragment few times, only first time we make it ourself
        if (savedInstanceState == null){
            launchRightMode()
        }

    }

    private fun launchRightMode() {
        val fragment = when (screenmode) {
            MODE_ADD -> ShopItemFragment.newInstAdd()
            MODE_EDIT -> ShopItemFragment.newInstEdit(shopItemId)
            else -> throw RuntimeException("screenmode wrong")

        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.frag_shopitem_container, fragment)
            //.addToBackStack(null)
            .commit()
    }

    private fun parseIntent(){
        val mode = intent.getStringExtra(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT){
            throw RuntimeException("SCREEN_MODE wrong")
        }
        screenmode = mode
        if (screenmode == MODE_EDIT){
            shopItemId = intent.getIntExtra(SHOP_ITEM_ID, SHOP_ITEM_ID_EMPTY)
            if (shopItemId == SHOP_ITEM_ID_EMPTY){
                throw RuntimeException("shopItemId == SHOP_ITEM_ID_EMPTY")
            }
        }
    }


    companion object {

        private const val SCREEN_MODE = "mode"
        private const val SHOP_ITEM_ID = "shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val SHOP_ITEM_ID_EMPTY = -1
        private const val MODE_EMPTY = ""


        fun newIntentAddItem(context: Context): Intent {

            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(SCREEN_MODE, MODE_ADD)
            return intent

        }

        fun newIntentEditItem(context: Context, id: Int): Intent {

            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(SCREEN_MODE, MODE_EDIT)
            intent.putExtra(SHOP_ITEM_ID, id)
            return intent
        }
    }
}