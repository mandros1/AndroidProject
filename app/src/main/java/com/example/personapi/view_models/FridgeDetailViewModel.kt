package com.example.personapi.view_models

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.personapi.models.FridgeItemViewModel

class FridgeDetailViewModel: ViewModel() {
    val fridge_item = MutableLiveData<FridgeItemViewModel>()
}