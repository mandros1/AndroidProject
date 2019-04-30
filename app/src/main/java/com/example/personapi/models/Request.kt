package com.example.personapi.models


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

data class Ingredient (
    val calories: Int,
    val name: String
)

data class Recipe (
    val description: String,
    val imgUrl: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val name: String,
    val rating: Int,
    val ratings: List<Int>
)