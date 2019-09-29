package com.anavol.auth_application

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface UserDataDao {
    @Query("SELECT * from userData")
    fun getAll(): List<UserData>
    @Insert(onConflict = REPLACE)
    fun insert(weatherData: UserData)
    @Query("DELETE from UserData")
    fun deleteAll()
}