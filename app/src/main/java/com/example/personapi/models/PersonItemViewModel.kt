package com.example.personapi.models

data class PersonItemViewModel(val person: Person){
    val name: String
    val shortName: String
    val gender: String
    val nat: String
    val completeString: String

    init {
        this.name = "${person.name.title.capitalize()} ${person.name.first.capitalize()} ${person.name.last.capitalize()}"
        this.shortName = person.name.last
        this.gender = person.gender
        this.nat = person.nat
        if(person.name.last != "Welcome") {
            this.completeString = "$name is a $gender from the country with the abbrev of $nat"
        } else {
            this.completeString = "Welcome to the random person app"
        }
    }
}