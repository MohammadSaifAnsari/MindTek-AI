package com.mohdsaifansari.mindtek.Database.ToolHistory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToolHistoryEntity(
    @PrimaryKey
    val idNo: Int,
    val prompt: String,
    val result: String,
    val day: String,
    val month: String,
    val year: String
)