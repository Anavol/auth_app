package com.anavol.auth_application.userDBTools

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserData::class], version = 1)
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDataDao(): UserDataDao

    companion object {
        private var INSTANCE: UserDataBase? = null
        fun getInstance(context: Context): UserDataBase? {
            if (INSTANCE == null) {
                synchronized(UserDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserDataBase::class.java, "users.db")
                        .build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}