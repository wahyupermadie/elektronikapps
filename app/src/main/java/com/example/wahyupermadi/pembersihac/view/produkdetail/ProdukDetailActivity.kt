package com.example.wahyupermadi.pembersihac.view.produkdetail

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.R.drawable.pembersih
import com.example.wahyupermadi.pembersihac.model.Produk
import com.example.wahyupermadi.pembersihac.view.editpembersih.EditPembersih
import com.example.wahyupermadi.pembersihac.view.editproduk.EditProduk
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_pembersih_detail.*
import kotlinx.android.synthetic.main.activity_produk_detail.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivity

class ProdukDetailActivity : AppCompatActivity() {
    lateinit var produk : Produk
    lateinit var dialog : Dialog
    lateinit var mDatabase : DatabaseReference
    lateinit var imageView : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produk_detail)

        produk = intent.getParcelableExtra("produk")
        val firebaseAuth = FirebaseAuth.getInstance()
        val key = firebaseAuth.currentUser?.uid
        mDatabase = FirebaseDatabase.getInstance().getReference("produk").child(key!!)

        swipe_produk.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                setData()
            }
        })

        setData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.pembersih_option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.edit_pembersih -> {
                startActivity<EditProduk>("produk" to produk)
            }
            R.id.delete_pembersih->{
                deleteProduk(produk.key)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteProduk(key: String?) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Are You Sure ?")
        alertDialog.setMessage("Are you sure want to delete this product ?")
        alertDialog.setPositiveButton("YES"){dialog, which ->
            mDatabase.child(key!!).removeValue()
            finish()
        }
        alertDialog.setNegativeButton("CANCEL"){dialog, which ->

        }
        val dialog : AlertDialog = alertDialog.create()
        dialog.show()
    }

    private fun setData() {
        Glide.with(ctx).load(produk.image).into(img_detproduk)
        tv_detinfoproduk.text = produk.info
        tv_detprodukharga.text = produk.price
        tv_detproduknama.text = produk.name

        dialog = Dialog(ctx)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layoutInflater.inflate(R.layout.img_layout, null))
        imageView = dialog.findViewById(R.id.img_detail)
        val close : ImageView = dialog.findViewById(R.id.close_image)
        Glide.with(ctx).load(produk.image).into(imageView)

        img_detproduk.setOnClickListener {
            dialog.show()
        }
        close.setOnClickListener {
            dialog.dismiss()
        }
    }
}
