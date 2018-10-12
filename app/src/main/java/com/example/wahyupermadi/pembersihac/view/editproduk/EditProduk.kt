package com.example.wahyupermadi.pembersihac.view.editproduk

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.bumptech.glide.Glide
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.model.Pembersih
import com.example.wahyupermadi.pembersihac.model.Produk
import com.example.wahyupermadi.pembersihac.view.editpembersih.EditPembersihPresenter
import com.example.wahyupermadi.pembersihac.view.pembersihdetail.PembersihDetailActivity
import com.example.wahyupermadi.pembersihac.view.produkdetail.ProdukDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_edit_produk.*

class EditProduk : AppCompatActivity(), EditProdukContract.View {
    private val IMAGE_REQUEST_CODE = 7

    lateinit var presenter : EditProductPresenter
    var filePathUri: Uri? = null
    lateinit var produk : Produk
    lateinit var mDatabase : DatabaseReference
    lateinit var mImageStorage : StorageReference
    lateinit var dialog : ProgressDialog
    var mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_produk)

        dialog = ProgressDialog(this)
        dialog.setMessage("Updating.....")
        dialog.setCancelable(false)

        produk = intent.getParcelableExtra("produk")
        mDatabase = FirebaseDatabase.getInstance().getReference("produk").child(mAuth.currentUser!!.uid).child(produk.key!!)
        mImageStorage = FirebaseStorage.getInstance().getReference("produk/")

        presenter = EditProductPresenter(this)
        setData()
    }

    private fun setData() {
        Glide.with(applicationContext).load(produk.image).into(edit_imagepr)
        edit_namapr.setText(produk.name)
        edit_hargapr.setText(produk.price)
        edit_infopr.setText(produk.info)

        edit_imagepr.setOnClickListener {
            intent = Intent()

            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), IMAGE_REQUEST_CODE);
        }

        tv_edit_imagePr.setOnClickListener {
            intent = Intent("com.android.camera.action.CROP")
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 128);
            intent.putExtra("outputY", 128);
            intent.putExtra("return-data", true);
            intent.setType("image/*")
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), IMAGE_REQUEST_CODE);
        }

        btn_predit.setOnClickListener {
            editProduk()
        }
    }

    private fun editProduk() {
        val name = edit_namapr.text.toString().trim()
        val price = edit_hargapr.text.toString().trim()
        val info = edit_infopr.text.toString().trim()
        val time = System.currentTimeMillis().toString()
        if(filePathUri != null)
        {
            val path =  time + "." + GetFileExtension(filePathUri!!)
            presenter.editProduct(produk.image!!, produk.toko!!, filePathUri!!, name, price, info, produk.key!!, mAuth.currentUser!!.uid, path)

        }else{
            val data : Uri? = null
            presenter.editProduct(produk.image!!, produk.toko!!, data , name, price, info, produk.key!!, mAuth.currentUser!!.uid, "")
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
                edit_imagepr.setImageBitmap(bitmap)

                // After selecting image change choose button above text.
                tv_edit_imagePr.setText("Image Selected")
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

    override fun hideProgressBar() {
        dialog.hide()
        newData()
    }

    override fun showProgressBar() {
        dialog.show()
    }
    private fun newData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(e: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                produk = p.getValue(Produk::class.java)!!
                val intent = Intent(applicationContext, ProdukDetailActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("produk", produk)
                startActivity(intent)
            }

        })
    }
}
