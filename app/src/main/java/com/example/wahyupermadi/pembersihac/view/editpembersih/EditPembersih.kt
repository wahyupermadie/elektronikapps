package com.example.wahyupermadi.pembersihac.view.editpembersih

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.widget.SwipeRefreshLayout
import android.webkit.MimeTypeMap
import com.bumptech.glide.Glide
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.model.Pembersih
import com.example.wahyupermadi.pembersihac.model.User
import com.example.wahyupermadi.pembersihac.view.fragmentpembersih.PembersihFragment
import com.example.wahyupermadi.pembersihac.view.pembersihdetail.PembersihDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_edit_pembersih.*
import kotlinx.android.synthetic.main.activity_pembersih_detail.*
import kotlinx.android.synthetic.main.activity_tambah_pembersih.*
import kotlinx.android.synthetic.main.activity_tambah_produk.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.lang.Exception
class EditPembersih : AppCompatActivity(), EditPembersihContract.View {
    private val IMAGE_REQUEST_CODE = 7
    lateinit var presenter : EditPembersihPresenter
    var filePathUri: Uri? = null
    lateinit var pembersih : Pembersih
    lateinit var mDatabase : DatabaseReference
    lateinit var mImageStorage : StorageReference
    lateinit var dialog : ProgressDialog
    var mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pembersih)
        supportActionBar?.setTitle("Edit Pembersih")

        dialog = ProgressDialog(this)
        dialog.setMessage("Updating.....")
        dialog.setCancelable(false)

        pembersih = intent.getParcelableExtra("pembersih")
        mDatabase = FirebaseDatabase.getInstance().getReference("pembersih").child(mAuth.currentUser!!.uid).child(pembersih.key!!)
        mImageStorage = FirebaseStorage.getInstance().getReference("pembersih/")

        presenter = EditPembersihPresenter(this)
        setData()
    }
    private fun setData() {
        Glide.with(applicationContext).load(pembersih.image).into(edit_imagepb)
        edit_namapb.setText(pembersih.name)
        edit_hargapb.setText(pembersih.price)
        edit_infopb.setText(pembersih.info)

        edit_imagepb.setOnClickListener {
            intent = Intent()

            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), IMAGE_REQUEST_CODE);
        }

        tv_edit_imagePb.setOnClickListener {
            intent = Intent()

            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), IMAGE_REQUEST_CODE);
        }

        btn_pbedit.setOnClickListener {
            editProduk()
        }
    }

    private fun newData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(e: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                pembersih = p.getValue(Pembersih::class.java)!!
                val intent = Intent(applicationContext, PembersihDetailActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("pembersih", pembersih)
                startActivity(intent)
            }

        })
    }

    override fun hideProgressBar() {
        dialog.hide()
        newData()
    }

    override fun showProgressBar() {
        dialog.show()
    }

    private fun editProduk() {
        val name = edit_namapb.text.toString().trim()
        val price = edit_hargapb.text.toString().trim()
        val info = edit_infopb.text.toString().trim()
        val time = System.currentTimeMillis().toString()
        if(filePathUri != null)
        {
            val path =  time + "." + GetFileExtension(filePathUri!!)
            presenter.editProduct(pembersih.image!!, pembersih.toko!!, filePathUri!!, name, price, info, pembersih.key!!, mAuth.currentUser!!.uid, path)

        }else{
            val data : Uri? = null
            presenter.editProduct(pembersih.image!!, pembersih.toko!!, data , name, price, info, pembersih.key!!, mAuth.currentUser!!.uid, "")
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
                edit_imagepb.setImageBitmap(bitmap)

                // After selecting image change choose button above text.
                tv_edit_imagePb.setText("Image Selected")
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
}
