package com.example.wahyupermadi.pembersihac.view.user.fragment_produk.pembersih

import com.example.wahyupermadi.pembersihac.model.Pembersih

interface CleanerContract {
    interface View{
        fun hideProgressBar()
        fun showProgressBar()
        fun ShowPembersih(pembersih: List<Pembersih>)
    }
    interface Presenter{
        fun getPembersihList()
    }
}