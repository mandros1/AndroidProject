package com.example.personapi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Customer(val uid: String?, val userName: String): Parcelable{
    constructor(): this("", "")
}