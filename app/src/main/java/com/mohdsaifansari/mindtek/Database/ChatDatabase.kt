package com.mohdsaifansari.mindtek.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChatEntity::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao


}

object ChatDatabaseProvider {
    lateinit var chatDatabase: ChatDatabase

    fun initialize(context: Context) {
        chatDatabase = Room.databaseBuilder(
            context.applicationContext,
            ChatDatabase::class.java,
            "chat_list_db1"
        ).build()
    }
}