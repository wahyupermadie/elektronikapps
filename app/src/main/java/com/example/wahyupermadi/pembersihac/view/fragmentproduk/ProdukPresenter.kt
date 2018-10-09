package com.example.wahyupermadi.pembersihac.view.fragmentproduk

import com.example.wahyupermadi.pembersihac.model.Pembersih
import com.example.wahyupermadi.pembersihac.model.Produk
import com.example.wahyupermadi.pembersihac.view.fragmentpembersih.PembersihContract
import com.google.firebase.database.*

class ProdukPresenter(val mView : ProdukContact.View) : ProdukContact.Presenter{
    lateinit var mDatabase: DatabaseReference
    var produkData : MutableList<Produk> = mutableListOf()
    override fun loadProduk(uid: String) {
        mView.showProgressBar()
        mDatabase = FirebaseDatabase.getInstance().getReference("produk").child(uid)
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                mView.hideProgressBar()
            }

            override fun onDataChange(p0: DataSnapshot) {
                produkData.clear()
                var i = 1
                for (data in p0!!.children){
                    val pembsh = data.getValue(Produk::class.java)
                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                    data.key?.let { pembsh?.key = it }

                    pembsh?.let { it -> produkData.add(it) }

                }
                mView.showProduk(produkData)
                mView.hideProgressBar()
            }

        })
    }

}