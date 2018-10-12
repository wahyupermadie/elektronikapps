package com.example.wahyupermadi.pembersihac.view.user.fragment_produk.pembersih

import android.content.ContentValues.TAG
import android.util.Log
import com.example.wahyupermadi.pembersihac.model.Pembersih
import com.example.wahyupermadi.pembersihac.model.User
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference

class CleanerPresenter(val mView : CleanerContract.View) : CleanerContract.Presenter {
    lateinit var mDatabaseUser : DatabaseReference
    lateinit var mDatabase : DatabaseReference
    lateinit var user : User
    var users : MutableList<User> = mutableListOf()
    var pembersihs : MutableList<Pembersih> = mutableListOf()
    override fun getPembersihList() {
        mView.showProgressBar()
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("user")
        mDatabaseUser.addValueEventListener( object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(datum : DataSnapshot in dataSnapshot.children){
                    users.add(datum.getValue(User::class.java)!!)
                }
                for (i in users){
                    mDatabase = FirebaseDatabase.getInstance().getReference("pembersih").child(i.uid.toString())
                    mDatabase.addValueEventListener(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            for (pembersih : DataSnapshot in p0.children){
                                pembersihs.add(pembersih.getValue(Pembersih::class.java)!!)
                                Log.d(TAG,"Hellllooooy"+ pembersih.getValue())
                            }
                            mView.ShowPembersih(pembersihs)
                            mView.hideProgressBar()
                        }

                    })
                }
            }

        })

    }
}