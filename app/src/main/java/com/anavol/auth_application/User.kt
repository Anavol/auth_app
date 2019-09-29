package com.anavol.auth_application

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var login: String,
    var photo: String
  //  var token: String,
  //  var socialNetwork: String
): Parcelable
