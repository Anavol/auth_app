package com.anavol.auth_application.login

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var login: String,
    var photo: String
): Parcelable
