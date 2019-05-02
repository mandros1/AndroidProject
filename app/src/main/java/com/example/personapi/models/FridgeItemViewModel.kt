package com.example.personapi.models

class FridgeItemViewModel(val fridge_item: FridgeItemsValue) {
    val amount: String
    val name: String
    val unit: String

    init {
        this.amount = fridge_item.amount
        this.name = fridge_item.name
        this.unit = fridge_item.unit
    }
}