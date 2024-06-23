package com.mohdsaifansari.mindtek.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Upsert
    suspend fun upsertChat(chat: ChatEntity)

    @Delete
    suspend fun deleteChat(chat: ChatEntity)

    @Query("DELETE FROM ChatEntity") // Replace 'chat_table' with youractual table name
    suspend fun deleteAllChats()

    @Query("SELECT * FROM ChatEntity ORDER BY timestamp")
    fun getAllChats(): Flow<List<ChatEntity>>
}