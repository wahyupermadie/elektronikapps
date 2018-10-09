package com.example.wahyupermadi.pembersihac.view.tambahpembersih

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.wahyupermadi.pembersihac.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_tambah_pembersih.*
import java.lang.Exception
import android.provider.MediaStore
import android.graphics.Bitmap
import android.webkit.MimeTypeMap
import android.app.ProgressDialog
import android.support.v4.app.FragmentManager
import android.util.Log
import android.widget.Toast
import com.example.wahyupermadi.pembersihac.model.Pembersih
import com.example.wahyupermadi.pembersihac.model.User
import com.google.android.gms.tasks.*
import com.google.firebase.database.*
import com.google.firebase.storage.UploadTask
import org.jetbrains.anko.toast
import com.google.firebase.auth.AuthResult
import org.jetbrains.anko.activityManager
import org.jetbrains.anko.ctx


class TambahPembersihActivity : AppCompatActivity() {
    var filePathUri: Uri? = null
    lateinit var mImageStorage : StorageReference
    lateinit var mAuth : FirebaseAuth
    lateinit var mDatabase : DatabaseReference
    private val IMAGE_REQUEST_CODE = 7
    var uid : String? = " "
    lateinit var user : User
    lateinit var dialog : ProgressDialog
    lateinit var fm : FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pembersih)

        dialog = ProgressDialog(this)
        dialog.setMessage("Uploading.....")
        dialog.setCancelable(false)

        fm = getSupportFragmentManager()


        uid = mAuth.currentUser?.uid
        mDatabase = FirebaseDatabase.getInstance().getReference("pembersih").child(uid!!)
        mImageStorage = FirebaseStorage.getInstance().getReference("pembersih/")

        val mToko = FirebaseDatabase.getInstance().getReference("user").child(uid!!)
        mToko.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(e: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                toast(""+p.getValue()+uid)
                user = p.getValue(User::class.java)!!
            }

        })
        add_gambarPb.setOnClickListener {
            intent = Intent()

            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), IMAGE_REQUEST_CODE);
        }

        tv_select_imagePb.setOnClickListener {
            intent = Intent()

            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), IMAGE_REQUEST_CODE);
        }

        btn_pbsubmit.setOnClickListener {
            uploadImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            filePathUri = data.data
            try {
                // Getting selected image into Bitmap.
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePathUri)

                // Setting up bitmap selected image into ImageView.
                add_gambarPb.setImageBitmap(bitmap)

                // After selecting image change choose button above text.
                tv_select_imagePb.setText("Image Selected")
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    private fun GetFileExtension(uri : Uri): String? {
        val contentResolver : ContentResolver
        contentResolver = getContentResolver()
        val mimeTypeMap = MimeTypeMap.getSingleton()

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun uploadImage(){
        dialog.show()
        if(filePathUri != null){
            val time = System.currentTimeMillis().toString()
            val path =  time + "." + GetFileExtension(filePathUri!!)
            val mImageStorage2 = mImageStorage.child(path)
            val uploadTask = mImageStorage2.putFile(filePathUri!!)
            val TAG = "URL GAMBAR"
            uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>>{
                override fun then(p0: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                    if (!p0.isSuccessful){
                        throw p0.exception!!
                    }

                    return mImageStorage2.downloadUrl
                }
            }).addOnCompleteListener( object : OnCompleteListener<Uri>{
                override fun onComplete(p0: Task<Uri>) {
                    if (p0.isSuccessful){
                        val downloadUrl : Uri
                        val tempImageName = pb_nama.text.toString().trim()
                        val price = pb_harga.text.toString().trim()
                        val info = pb_info.text.toString().trim()
                        val toko = user.name
                        downloadUrl = p0.getResult()!!
                        Log.d(TAG,""+downloadUrl)
                        val uploadData = Pembersih(tempImageName, price, info, downloadUrl.toString(),toko)
                        val dataId = mDatabase.push().key
                        mDatabase.child(dataId!!).setValue(uploadData)
                            dialog.dismiss()
                            Toast.makeText(applicationContext, "Successfully Uploaded :)", Toast.LENGTH_LONG).show()
                            fm.popBackStack()
                    }
                }

            })
        }
    }
}
