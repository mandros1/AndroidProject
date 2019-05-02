package com.example.personapi.models

class IngredientsItemViewModel(val ingredient: IngredientElement){
    val name: String
    val calories: String
    val unit: String

    init {
        this.name = ingredient.name
        this.calories = ingredient.calories
        this.unit = ingredient.unit
    }
}