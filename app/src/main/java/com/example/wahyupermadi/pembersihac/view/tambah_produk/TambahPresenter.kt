package com.example.wahyupermadi.pembersihac.view.tambah_produk

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.wahyupermadi.pembersihac.model.Pembersih
import com.example.wahyupermadi.pembersihac.model.Produk
import com.example.wahyupermadi.pembersihac.model.User
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_tambah_pembersih.*
import org.jetbrains.anko.toast

class TambahPresenter(val mVew : TambahContract.View) : TambahContract.Presenter{
    lateinit var mImageStorage : StorageReference
    lateinit var mDatabase : DatabaseReference
    lateinit var user : User
    override fun storeProduk(name: String, price: String, info: String, image: Uri, key: String, path: String) {
        mDatabase = FirebaseDatabase.getInstance().getReference("produk").child(key)
        mImageStorage = FirebaseStorage.getInstance().getReference("produk/")

        mVew.showProgressBar()
        val mImageStorage2 = mImageStorage.child(path)
        val uploadTask = mImageStorage2.putFile(image)
        val TAG = "URL GAMBAR"

        val mToko = FirebaseDatabase.getInstance().getReference("user").child(key)

        mToko.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(e: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                user = p.getValue(User::class.java)!!
            }

        })

        uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
            override fun then(p0: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                if (!p0.isSuccessful){
                    throw p0.exception!!
                }

                return mImageStorage2.downloadUrl
            }
        }).addOnCompleteListener( object : OnCompleteListener<Uri> {
            override fun onComplete(p0: Task<Uri>) {
                if (p0.isSuccessful){
                    val downloadUrl : Uri
                    val toko = user.name
                    downloadUrl = p0.getResult()!!
                    Log.d(TAG,""+downloadUrl)
                    val uploadData = Produk(name, price, info, downloadUrl.toString(), toko)
                    val dataId = mDatabase.push().key
                    mDatabase.child(dataId!!).setValue(uploadData)
                    mVew.hideProgressBar()
                }
                else{
                    mVew.hideProgressBar()
                }
            }

        })
    }
}