package com.example.personapi.models

class IngredientsItemViewModel(val ingredient: Ingredient){
    val name: String
    val calories: Int

    init {
        this.name = ingredient.name
        this.calories = ingredient.calories
    }
}