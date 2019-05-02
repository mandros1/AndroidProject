package com.example.personapi.models

class RecipeItemViewModel(val recipe: RecipesValue){
    val description: String
    val imgUrl: String
    val ingredients: List<Ingredient>
    val instructions: List<String>
    val name: String
    val rating: Long
    val ratings: List<Long>

    init {
        this.description = recipe.description
        this.imgUrl = recipe.imgURL
        this.ingredients = recipe.ingredients
        this.instructions = recipe.instructions
        this.name = recipe.name
        this.rating = recipe.rating
        this.ratings = recipe.ratings
    }
}