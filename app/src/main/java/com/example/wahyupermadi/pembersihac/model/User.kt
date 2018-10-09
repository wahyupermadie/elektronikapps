package com.example.wahyupermadi.pembersihac.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User() : Parcelable{
    var name : String? = null
    var email : String? = null
    var phone : String? = null
    var uid : String? = null
    var image : String? = null
    var lng : String? = null
    var lat : String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        email = parcel.readString()
        phone = parcel.readString()
        uid = parcel.readString()
        image = parcel.readString()
        lng = parcel.readString()
        lat = parcel.readString()
    }


    constructor(image: String?, name : String?, email : String?, phone : String?, uid : String?, lng : String, lat :String) : this() {
        this.image = image
        this.name = name
        this.email = email
        this.phone = phone
        this.uid = uid
        this.lat = lat
        this.lng = lng
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(uid)
        parcel.writeString(image)
        parcel.writeString(lng)
        parcel.writeString(lat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}