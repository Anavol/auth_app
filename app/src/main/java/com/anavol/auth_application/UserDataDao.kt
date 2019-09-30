package com.anavol.auth_application

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface UserDataDao {

    @Query("SELECT * FROM userData")
    suspend fun getAll(): List<UserData>

    @Insert(onConflict = REPLACE)
    suspend fun insert(userData: UserData): Long

    @Query("DELETE from userData")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteUser(user: UserData): Int
}