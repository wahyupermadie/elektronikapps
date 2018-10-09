package com.example.wahyupermadi.pembersihac.model

import android.os.Parcel
import android.os.Parcelable

class Produk() : Parcelable {
    var name: String? = null
    var price : String? = null
    var image : String? = null
    var toko : String? = null
    var key : String? = null
    var info : String? = null

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        price = parcel.readString()
        image = parcel.readString()
        toko = parcel.readString()
        key = parcel.readString()
        info = parcel.readString()
    }


    constructor(name: String?, price: String?, info : String?, image: String?, toko: String?) : this() {
        this.name = name
        this.image = image
        this.price = price
        this.info = info
        this.toko = toko
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeString(image)
        parcel.writeString(toko)
        parcel.writeString(key)
        parcel.writeString(info)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Produk> {
        override fun createFromParcel(parcel: Parcel): Produk {
            return Produk(parcel)
        }

        override fun newArray(size: Int): Array<Produk?> {
            return arrayOfNulls(size)
        }
    }
}