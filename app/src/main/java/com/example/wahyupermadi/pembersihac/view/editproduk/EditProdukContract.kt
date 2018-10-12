package com.example.wahyupermadi.pembersihac.view.editproduk

import android.net.Uri

interface EditProdukContract{
    interface View{
        fun hideProgressBar()
        fun showProgressBar()
    }
    interface Presenter{
        fun editProduct(imageUrl:String, tokoNama:String, image: Uri?, name:String, price:String, info:String, key:String, uid:String, path:String)
    }
}