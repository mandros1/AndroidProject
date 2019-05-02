package com.example.personapi.view_models

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.personapi.models.ShoppingItemViewModel

class ShoppingDetailViewModel: ViewModel() {
    val shopping_item = MutableLiveData<ShoppingItemViewModel>()
}