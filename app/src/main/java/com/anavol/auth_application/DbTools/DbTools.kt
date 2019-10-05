package com.anavol.auth_application.DbTools

import android.content.Context

class DbTools(context: Context) {
    private var mDb: UserDataBase? = null
    private var userDataDao: UserDataDao?= null
    init {
        mDb = UserDataBase.getInstance(context)
        if (mDb != null){
            userDataDao = mDb!!.userDataDao()
        }
        else error("failed to get instance of database")
    }

        suspend fun fetchUserDataFromDb(userData: UserData) {
            val userDataList =
                userDataDao?.getAll()
            if (userDataList == null || userDataList.isEmpty()) {
            }
            else {
                userData.id = userDataList[0].id
                userData.name = userDataList[0].name
                userData.photo = userDataList[0].photo
                userData.token = userDataList[0].token
                userData.socialNetwork = userDataList[0].socialNetwork
            }
        }

        suspend fun insertUserDataInDb(userData: UserData) {
            userDataDao?.insert(userData)
        }

        suspend fun clearDb() {
            userDataDao?.deleteAll()
        }
    }
