package com.example.wahyupermadi.pembersihac.view.user.fragment_toko

import com.example.wahyupermadi.pembersihac.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TokoPresenter(val mView : TokoContract.View) : TokoContract.Presenter{
    lateinit var user : User
    var users : MutableList<User> = mutableListOf()
    override fun getToko() {
        mView.showLoading()
        val mDatabase = FirebaseDatabase.getInstance().getReference("user")
        mDatabase.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                mView.hideLoading()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                users.clear()
                for(datum : DataSnapshot in dataSnapshot.children){
                    users.add(datum.getValue(User::class.java)!!)
                }
                mView.showToko(users)
                mView.hideLoading()
            }
        })
    }
}