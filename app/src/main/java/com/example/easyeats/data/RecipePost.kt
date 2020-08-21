package com.example.easyeats.data

import com.google.firebase.Timestamp

data class RecipePost (
    var id: String = "",
    val userId: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val title: String = "",
    val headerImageUrl: String ="",
    val description: String = "",
    val ingredients: String ="",
    val instructions: String =""
)

