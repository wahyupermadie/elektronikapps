package com.example.wahyupermadi.pembersihac.view.user.fragment_produk.produk

import com.example.wahyupermadi.pembersihac.model.Produk

interface AcContract {
    interface View{
        fun hideProgressBar()
        fun showProgressBar()
        fun ShowAc(produk : List<Produk>)
    }
    interface Presenter{
        fun getProductList()
    }
}