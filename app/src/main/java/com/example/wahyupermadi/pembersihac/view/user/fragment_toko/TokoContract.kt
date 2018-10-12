package com.example.wahyupermadi.pembersihac.view.user.fragment_toko

import com.example.wahyupermadi.pembersihac.model.User

interface TokoContract{
    interface View{
        fun showLoading()
        fun hideLoading()
        fun showToko(toko : List<User>)
    }
    interface Presenter{
        fun getToko()
    }
}