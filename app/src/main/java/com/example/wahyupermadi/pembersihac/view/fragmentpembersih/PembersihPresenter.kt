package com.example.wahyupermadi.pembersihac.view.fragmentpembersih

import android.content.ContentValues.TAG
import android.util.Log
import com.example.wahyupermadi.pembersihac.model.Pembersih
import com.google.firebase.database.*

class PembersihPresenter(val mView : PembersihContract.View) : PembersihContract.Presenter{
    lateinit var mDatabase: DatabaseReference
    var pembersihData : MutableList<Pembersih> = mutableListOf()
    var dataPembersih : ArrayList<Pembersih> = ArrayList()
    override fun loadPembersih(uid: String) {
        mView.showProgressBar()
        mDatabase = FirebaseDatabase.getInstance().getReference("pembersih").child(uid)
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                mView.hideProgressBar()
            }

            override fun onDataChange(p0: DataSnapshot) {
                dataPembersih = ArrayList()
                pembersihData.clear()
                var i = 1
                for (data in p0!!.children){
                    val pembsh = data.getValue(Pembersih::class.java)
                    //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                    data.key?.let { pembsh?.key = it }

                    pembsh?.let { it -> pembersihData.add(it) }

                }
                mView.showPembersih(pembersihData)
                mView.hideProgressBar()
            }

        })
    }

}