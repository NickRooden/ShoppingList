package com.myrood.shoppinglist.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myrood.shoppinglist.data.ShopListRepositoryImpl
import com.myrood.shoppinglist.domain.*
import java.lang.Exception

class ShopItemViewModel: ViewModel() {

    private val repositoryImpl = ShopListRepositoryImpl

    private val editShopItemUseCase = EditShopItemUseCase(repositoryImpl)
    private val addShopItemUseCase = AddShopItemUseCase(repositoryImpl)
    private val getShopItemUseCase = GetShopItemUseCase(repositoryImpl)

    private var _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private var _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private  var _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private var _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun editShopItem(inputName: String?, inputCount: String?){

        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val validatation = validateInput(name, count)
        Log.d("editshopitem metod","before validation")
        if (validatation){
            Log.d("editshopitem metod","validation is done")

            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(item)
                finishWork()
            }

        }

    }

    fun addShopItem(inputName: String?, inputCount: String?){

        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val validatation = validateInput(name, count)

        if (validatation){
            val  shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            finishWork()
        }

    }

    fun getShopItem(shopItemId: Int){
        val item = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = item
    }

    private fun parseName(inputName: String?): String{
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int{

        return try {
            inputCount?.trim()?.toInt() ?: 0
        }catch (e: Exception){
            0
        }
    }
    private fun validateInput(name: String, count: Int): Boolean{

        var result = true
        if (name.isBlank() or (name == "")){
            _errorInputName.value = true
            result = false
        }
        if (count <= 0){
            _errorInputCount.value = true
            result = false
        }
        return result
    }
    fun resetErrorInputName(){
        _errorInputName.value = false
    }
    fun resetErrorInputCount(){
        _errorInputCount.value = false
    }
    private fun finishWork(){
        _shouldCloseScreen.value = Unit
    }
}