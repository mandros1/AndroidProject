package com.example.personapi.models

data class RecipeItemViewModel(val recipe: RecipeValue){
    val description: String
    val imgUrl: String
    val ingredients: String
    val instructions: List<String>
    val name: String
    val rating: Long
    val ratings: Float
    val stringInstructions: String
    val stringAmounts: String
    val stringUnits: String

    init {
        this.name = recipe.name
        this.rating = recipe.rating
        var sum = 0
        for(rating in recipe.ratings){
            sum += rating.toInt()
        }
        this.ratings = sum/recipe.ratings.size.toFloat()
        this.instructions = recipe.instructions

        var initial = true
        var ingredientString = ""
        var amountString = ""
        var unitString = ""
        for(ingredient in recipe.ingredients){
            if (!initial){
                ingredientString += "#|# ${ingredient.name}"
                amountString += "#|# ${ingredient.amount}"
                unitString += "#|# ${ingredient.unit}"
            }else{
                initial = false
                ingredientString += ingredient.name
                amountString += ingredient.amount
                unitString += ingredient.unit
            }
        }
        var instrString = ""
        initial = true
        for(instruction in recipe.instructions){
            if (!initial){
                instrString += "#|# ${instruction}"
            }else{
                initial = false
                instrString += instruction
            }
        }
        this.stringAmounts = amountString
        this.stringUnits = unitString
        this.stringInstructions = instrString
        this.ingredients = ingredientString
        this.imgUrl = recipe.imgUrl
        this.description = recipe.description
    }
}