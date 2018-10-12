package com.example.wahyupermadi.pembersihac.view.tambah_produk

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.webkit.MimeTypeMap
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_tambah_pembersih.*
import kotlinx.android.synthetic.main.activity_tambah_produk.*
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.toast
import java.lang.Exception
import java.util.jar.Manifest

class TambahProdukActivity : AppCompatActivity(), TambahContract.View {
    var filePathUri: Uri? = null

    lateinit var mImageStorage : StorageReference
    lateinit var mAuth : FirebaseAuth
    lateinit var mDatabase : DatabaseReference
    private val IMAGE_REQUEST_CODE = 7
    var uid : String? = " "
    var RequestPermissionCode = 1;
    lateinit var user : User
    lateinit var CropIntent : Intent
    lateinit var presenter: TambahPresenter
    lateinit var dialog : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_produk)
        dialog = ProgressDialog(this)
        dialog.setMessage("Uploading.....")
        dialog.setCancelable(false)

        mAuth = FirebaseAuth.getInstance()
        uid = mAuth.currentUser?.uid
        presenter = TambahPresenter(this)


        add_gambarPr.setOnClickListener {
            intent = Intent("com.android.camera.action.CROP")
            intent.putExtra("crop", "true");
            //indicate aspect of desired crop
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //indicate output X and Y
            intent.putExtra("outputX", 256);
            intent.putExtra("outputY", 256);
            //retrieve data on return
            intent.putExtra("return-data", true);
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), IMAGE_REQUEST_CODE);
        }

        tv_select_imagePr.setOnClickListener {
            intent = Intent("com.android.camera.action.CROP")

            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), IMAGE_REQUEST_CODE);
        }

        btn_prsubmit.setOnClickListener {
            storeProduk()
        }
    }


    private fun storeProduk() {
        val name = pr_nama.text.toString().trim()
        val price = pr_harga.text.toString().trim()
        val info = pr_info.text.toString().trim()
        val time = System.currentTimeMillis().toString()
        val path =  time + "." + GetFileExtension(filePathUri!!)
        if(filePathUri != null){
            presenter.storeProduk(name, price, info, filePathUri!!, uid!!, path)
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
                add_gambarPr.setImageBitmap(bitmap)

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


    override fun hideProgressBar() {
        dialog.hide()
        finish()
    }

    override fun showProgressBar() {
        dialog.show()
    }
}
