package com.example.wahyupermadi.pembersihac.view.tambah_produk

import android.net.Uri

interface TambahContract{
    interface View{
        fun hideProgressBar()
        fun showProgressBar()
    }
    interface Presenter{
        fun storeProduk(name : String, price:String, info:String, image: Uri, key:String, path:String)
    }
}