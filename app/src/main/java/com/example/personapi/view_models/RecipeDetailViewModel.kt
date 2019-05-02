package com.example.personapi.view_models

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.personapi.models.RecipeItemViewModel

class RecipeDetailViewModel: ViewModel() {
    val recipe = MutableLiveData<RecipeItemViewModel>()
}