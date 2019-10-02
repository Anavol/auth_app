package com.anavol.auth_application.userDBTools

class DbTools {
    companion object{
        suspend fun fetchUserDataFromDb(mDb: UserDataBase?, userData: UserData) {
            val userDataList =
                mDb?.userDataDao()?.getAll()
            if (userDataList == null || userDataList?.size == 0) {
            }
            else {
                userData.id = userDataList?.get(0).id
                userData.name = userDataList?.get(0).name
                userData.photo = userDataList?.get(0).photo
                userData.token = userDataList?.get(0).token
                userData.socialNetwork = userDataList?.get(0).socialNetwork
            }
        }

        suspend fun insertUserDataInDb(mDb: UserDataBase?, userData: UserData) {
            mDb?.userDataDao()?.insert(userData)
        }

        suspend fun clearDb(mDb: UserDataBase?) {
            mDb?.userDataDao()?.deleteAll()
        }
    }
}