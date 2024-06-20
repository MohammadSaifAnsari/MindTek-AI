package com.mohdsaifansari.mindtek.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User::class],
    version = 2
)
abstract class UserDatabase : RoomDatabase() {
    abstract val dao: UserDao
}

object DatabaseProvider {
    lateinit var userDatabase: UserDatabase


    fun initialize(context: Context) {
        userDatabase = Room.databaseBuilder(
            context.applicationContext,
            UserDatabase::class.java,
            "user_db2"
        ).build()
    }
}