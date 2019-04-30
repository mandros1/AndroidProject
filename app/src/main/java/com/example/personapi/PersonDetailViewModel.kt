package com.example.personapi

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.personapi.models.PersonItemViewModel

class PersonDetailViewModel: ViewModel() {
    val person = MutableLiveData<PersonItemViewModel>()
}