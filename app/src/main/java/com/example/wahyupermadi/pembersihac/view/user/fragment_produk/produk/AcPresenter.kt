package com.example.wahyupermadi.pembersihac.view.user.fragment_produk.produk

import android.content.ContentValues.TAG
import android.util.Log
import com.example.wahyupermadi.pembersihac.model.Produk
import com.example.wahyupermadi.pembersihac.model.User
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference

class AcPresenter(val mView : AcContract.View) : AcContract.Presenter {
    lateinit var mDatabaseUser : DatabaseReference
    lateinit var mDatabase : DatabaseReference
    lateinit var mStorage : StorageReference
    lateinit var user : User
    var users : MutableList<User> = mutableListOf()
    var produks : MutableList<Produk> = mutableListOf()
    override fun getProductList() {
        mView.showProgressBar()
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("user")
        mDatabaseUser.addValueEventListener( object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(datum : DataSnapshot in dataSnapshot.children){
                    users.add(datum.getValue(User::class.java)!!)
                }
                for (i in users){
                    mDatabase = FirebaseDatabase.getInstance().getReference("produk").child(i.uid.toString())
                    mDatabase.addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            for (produk : DataSnapshot in p0.children){
                                produks.add(produk.getValue(Produk::class.java)!!)
                                Log.d(TAG,"Hellllooooy"+ produk.getValue())
                            }
                            mView.ShowAc(produks)
                            mView.hideProgressBar()
                        }

                    })
                }
            }

        })

    }
}