package com.anavol.auth_application.userDBTools

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userData")
data class UserData(@PrimaryKey(autoGenerate = true) var id: Long? = null,
                    @ColumnInfo(name = "name") var name: String = "",
                    @ColumnInfo(name = "photo") var photo: String = "",
                    @ColumnInfo(name = "token") var token: String = "",
                    @ColumnInfo(name = "socialNetwork") var socialNetwork: String = "")
