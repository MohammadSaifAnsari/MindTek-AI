package com.mohdsaifansari.mindtek.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM User ORDER BY id DESC LIMIT 1")
    suspend fun getLatestUser(): User?
}