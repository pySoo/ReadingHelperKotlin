package com.example.readinghelper.data.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(var uid:String, var name:String) : Parcelable {

    var profilePath: String? = null

    @Exclude
    var isNew = false

    @Exclude
    var isCreated = false
}