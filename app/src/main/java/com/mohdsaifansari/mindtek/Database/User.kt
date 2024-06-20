package com.mohdsaifansari.mindtek.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val id: Int,

    val firstName: String,
    val lastName: String,
    val email: String,
    val profilePicture: ByteArray? = null

)