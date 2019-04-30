package com.example.personapi.models

class RecipeItemViewModel(val recipe: Recipe){
    val description: String
    val imgUrl: String
    val ingredients: List<String>
    val instructions: List<String>
    val name: String
    val rating: Int
    val ratings: List<Int>

    init {
        this.description = recipe.description
        this.imgUrl = recipe.imgUrl
        this.ingredients = recipe.ingredients
        this.instructions = recipe.instructions
        this.name = recipe.name
        this.rating = recipe.rating
        this.ratings = recipe.ratings
    }
}