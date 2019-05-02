package com.example.personapi.view_models

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.personapi.models.IngredientsItemViewModel

class IngredientDetailViewModel: ViewModel() {
    val ingredient = MutableLiveData<IngredientsItemViewModel>()
}