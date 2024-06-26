package com.mohdsaifansari.mindtek.Database.ToolHistory

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolHistoryDao {

    @Upsert
    suspend fun upsertToolHistory(toolHistory: ToolHistoryEntity)

    @Query("SELECT result FROM ToolHistoryEntity WHERE idNo = :id")
    suspend fun getToolHistoryById(id: Int): String

    @Delete
    suspend fun deleteToolHistory(toolHistory: ToolHistoryEntity)

    @Query("DELETE FROM ToolHistoryEntity WHERE idNo = :id")
    suspend fun deleteToolHistoryById(id: Int)

    @Query("DELETE FROM ToolHistoryEntity")
    suspend fun deleteAllToolHistories()

    @Query("SELECT * FROM ToolHistoryEntity ORDER BY idNo")
    fun getAllToolHistories(): Flow<List<ToolHistoryEntity>>
}