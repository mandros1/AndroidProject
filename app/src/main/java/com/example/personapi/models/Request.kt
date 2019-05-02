package com.example.personapi.models

import android.os.Parcel
import android.os.Parcelable


data class Request (
    val results: List<Person>,
    val info: Info
)

data class Info (
    val seed: String,
    val results: Long,
    val page: Long,
    val version: String
)

data class Person (
    val gender: String,
    val name: Name,
    val nat: String
)


data class Name (
    val title: String,
    val first: String,
    val last: String
)

typealias Ingredients = HashMap<String, IngredientElement>

data class IngredientElement (
    val calories: String,
    val name: String,
    val unit: String
)


typealias Recipe = HashMap<String, RecipeValue>

data class RecipeValue(
    val description: String,
    val imgUrl: String,
    val ingredients: List<Ingredient>,
    val instructions: List<String>,
    val name: String,
    val rating: Long,
    val ratings: List<Long>
)

data class Ingredient (
    val amount: String,
    val name: String,
    val unit: String
)

typealias ShoppingList = HashMap<String, ShoppingListValue>

data class ShoppingListValue (
    val amount: String,
    val name: String,
    val unit: String
)

typealias FridgeItems = HashMap<String, FridgeItemsValue>

data class FridgeItemsValue (
    val amount: String,
    val name: String,
    val unit: String
)
