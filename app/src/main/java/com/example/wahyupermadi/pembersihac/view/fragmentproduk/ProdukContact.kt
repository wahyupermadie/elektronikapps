package com.example.wahyupermadi.pembersihac.view.fragmentproduk
import com.example.wahyupermadi.pembersihac.model.Produk

interface ProdukContact{
    interface View{
        fun hideProgressBar()
        fun showProgressBar()
        fun showProduk(produk : List<Produk>)
    }
    interface Presenter{
        fun loadProduk(uid : String)
    }
}