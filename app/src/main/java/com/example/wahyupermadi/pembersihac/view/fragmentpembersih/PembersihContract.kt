package com.example.wahyupermadi.pembersihac.view.fragmentpembersih

import com.example.wahyupermadi.pembersihac.model.Pembersih

interface PembersihContract{
    interface View{
        fun showPembersih(pembersih : List<Pembersih>)
        fun hideProgressBar()
        fun showProgressBar()
    }
    interface Presenter{
        fun loadPembersih(uid : String)
    }
}