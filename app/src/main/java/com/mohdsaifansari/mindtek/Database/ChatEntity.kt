package com.mohdsaifansari.mindtek.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatEntity(
    @PrimaryKey
    val timestamp: Long,
    val message: String,
    val imageAddress: String,
    val isUser: Boolean

)

