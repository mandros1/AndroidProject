package com.example.personapi.models

class ShoppingItemViewModel(val shoppingItem: ShoppingListValue) {

    val amount: String
    val name: String
    val unit: String

    init {
        this.amount = shoppingItem.amount
        this.name = shoppingItem.name
        this.unit = shoppingItem.unit
    }
}