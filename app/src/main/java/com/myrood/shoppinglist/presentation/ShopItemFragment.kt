package com.myrood.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.myrood.shoppinglist.R
import com.myrood.shoppinglist.databinding.ShopItemFragBinding

class ShopItemFragment : Fragment() {

    private var _bind: ShopItemFragBinding? = null
    private val bind: ShopItemFragBinding get() = _bind!!

    private var screenmode: String = MODE_EMPTY
    private var shopItemId = SHOP_ITEM_ID_EMPTY

    private lateinit var viewModel: ShopItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bind = ShopItemFragBinding.inflate(inflater,container,false)
        return bind.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]


        addTextChangeListeners()

        launchRightMode()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {

            val msg = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            bind.tilName.error = msg
        }
        viewModel.errorInputCount.observe(viewLifecycleOwner) {

            val msg = if (it) {
                getString(R.string.error_input_count)
            } else {
                null
            }
            bind.tilCount.error = msg
        }
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            requireActivity().onBackPressed()
        }
    }

    private fun launchRightMode() {
        when (screenmode) {
            MODE_ADD -> launchAddScreen()
            MODE_EDIT -> launchEditScreen()

        }
    }

    private fun addTextChangeListeners() {
        bind.exName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        bind.exCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }


    private fun launchEditScreen(){

        viewModel.getShopItem(shopItemId)

        viewModel.shopItem.observe(viewLifecycleOwner){

            bind.exName.setText(it.name)
            bind.exCount.setText(it.count.toString())
        }
        bind.saveButton.setOnClickListener {
            viewModel.editShopItem(bind.exName.text?.toString(), bind.exCount.text?.toString())
            //Log.d("button","edit button pushed")
        }


    }

    private fun launchAddScreen(){

        bind.saveButton.setOnClickListener {
            viewModel.addShopItem(bind.exName.text?.toString(), bind.exCount.text?.toString())
        }

    }


    private fun parseArgs(){

        val args = requireArguments()

        if (!args.containsKey(SCREEN_MODE)){
            throw RuntimeException("Where is SCREEN_MODE?")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT){
            throw RuntimeException("Where is MODE_ADD or MODE_EDIT?")
        }
        screenmode = mode
        if (screenmode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Where is SHOP_ITEM_ID?")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID)
        }

    }

    companion object{

        private const val SCREEN_MODE = "mode"

        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val SHOP_ITEM_ID_EMPTY = -1
        private const val MODE_EMPTY = ""

        private const val SHOP_ITEM_ID = "shop_item_id"

        fun newInstAdd() =
            ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }

        fun newInstEdit(itemId: Int) =
           ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, itemId)
                }
            }


    }


    override fun onDestroy() {
        super.onDestroy()
        _bind = null
    }
}