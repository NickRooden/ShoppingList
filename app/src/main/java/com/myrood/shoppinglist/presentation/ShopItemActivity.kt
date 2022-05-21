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

class ShopItemActivity : AppCompatActivity() {

    private var screenmode = MODE_EMPTY
    private var shopItemId = EXTRA_SHOP_ITEM_ID_EMPTY


    private lateinit var tilName: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var tilCount: TextInputLayout
    private lateinit var etCount: EditText
    private lateinit var saveButton: Button


    private lateinit var viewModel: ShopItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        parseIntent()

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        initViews()

        addTextChangeListeners()

        launchRightMode()
        observeViewModel()


    }

    private fun observeViewModel() {
        viewModel.errorInputName.observe(this) {

            val msg = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            tilName.error = msg
        }
        viewModel.errorInputCount.observe(this) {

            val msg = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            tilCount.error = msg
        }
        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }

    private fun launchRightMode() {
        when (screenmode) {
            MODE_ADD -> launchAddScreen()
            MODE_EDIT -> launchEditScreen()

        }
    }

    private fun addTextChangeListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun initViews() {
        tilName = findViewById(R.id.til_name)
        etName = findViewById(R.id.ex_name)
        tilCount = findViewById(R.id.til_count)
        etCount = findViewById(R.id.ex_count)
        saveButton = findViewById(R.id.save_button)
    }

    private fun launchEditScreen(){

        viewModel.getShopItem(shopItemId)

        viewModel.shopItem.observe(this){

            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        saveButton.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
            //Log.d("button","edit button pushed")
        }


    }

    private fun launchAddScreen(){

        saveButton.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }

    }


    private fun parseIntent(){

        if (!intent.hasExtra(EXTRA_SCREEN_MODE)){
            throw RuntimeException("Where is EXTRA_SCREEN_MODE?")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT){
            throw RuntimeException("Where is MODE_ADD or MODE_EDIT?")
        }
        screenmode = mode
        if (screenmode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Where is EXTRA_SHOP_ITEM_ID?")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, EXTRA_SHOP_ITEM_ID_EMPTY)
        }

    }

    companion object{

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val EXTRA_SHOP_ITEM_ID_EMPTY = -1
        private const val MODE_EMPTY = ""


        fun newIntentAddItem(context: Context): Intent{

            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent

        }
        fun newIntentEditItem(context: Context, id: Int): Intent{

            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, id)
            return intent
        }
    }
}