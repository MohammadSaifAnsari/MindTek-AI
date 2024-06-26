package com.mohdsaifansari.mindtek.Database.ToolHistory

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ToolHistoryEntity::class], version = 1)
abstract class ToolHistoryDatabase : RoomDatabase() {
    abstract fun toolHistoryDao(): ToolHistoryDao
}

object ToolHistoryDatabaseProvider {
    lateinit var toolHistoryDatabase: ToolHistoryDatabase

    fun initialize(context: Context) {
        toolHistoryDatabase = Room.databaseBuilder(
            context.applicationContext,
            ToolHistoryDatabase::class.java,
            "tool_history_db"
        ).build()
    }
}