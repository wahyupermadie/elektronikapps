package com.example.wahyupermadi.pembersihac.view.editpembersih

import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.example.wahyupermadi.pembersihac.model.Pembersih
import com.example.wahyupermadi.pembersihac.model.Produk
import com.example.wahyupermadi.pembersihac.model.User
import com.firebase.client.Firebase
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class EditPembersihPresenter(val mView:EditPembersihContract.View):EditPembersihContract.Presenter{
    lateinit var mDatabase : DatabaseReference
    lateinit var mImageStorage : StorageReference
    lateinit var pembersih: Pembersih
    override fun editProduct(imageUrl:String, tokoNama:String, image: Uri?, name: String, price: String, info: String, key: String, uid:String, path:String) {
        mView.showProgressBar()
        mDatabase = FirebaseDatabase.getInstance().getReference("pembersih").child(uid).child(key)
        mImageStorage = FirebaseStorage.getInstance().getReference("pembersih/")

        if (image == null)
        {
            val uploadData = Produk(name, price, info, imageUrl, tokoNama)
            mDatabase.setValue(uploadData)
            mView.hideProgressBar()
        }else{
            val mImageStorage2 = mImageStorage.child(path)
            val uploadTask = mImageStorage2.putFile(image)


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
                        downloadUrl = p0.getResult()!!
                        val uploadData = Produk(name, price, info, downloadUrl.toString(), tokoNama)
                        mDatabase.setValue(uploadData)
                        mView.hideProgressBar()
                    }
                    else{
                        mView.hideProgressBar()
                    }
                }

            })
        }
    }
}