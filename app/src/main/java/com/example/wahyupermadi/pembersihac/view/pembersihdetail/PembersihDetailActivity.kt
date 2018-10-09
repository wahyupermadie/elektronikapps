package com.example.wahyupermadi.pembersihac.view.pembersihdetail

import android.app.AlertDialog
import android.app.Dialog
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.*
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.model.Pembersih
import com.example.wahyupermadi.pembersihac.view.editpembersih.EditPembersih
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_pembersih_detail.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class PembersihDetailActivity : AppCompatActivity() {
    lateinit var pembersih : Pembersih
    lateinit var dialog : Dialog
    lateinit var imageView : ImageView
    lateinit var mDatabase : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembersih_detail)
        pembersih = intent.getParcelableExtra("pembersih")
        val firebaseAuth = FirebaseAuth.getInstance()
        val key = firebaseAuth.currentUser?.uid
        mDatabase = FirebaseDatabase.getInstance().getReference("pembersih").child(key!!)

        swipe_pembersih.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
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
                startActivity<EditPembersih>("pembersih" to pembersih)
            }
            R.id.delete_pembersih->{
                deletePembersih(pembersih.key)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deletePembersih(key: String?) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Are You Sure ?")
        alertDialog.setMessage("Are you sure want to delete this cleaner ?")
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
        Glide.with(ctx).load(pembersih.image).into(img_detpembersih)
        tv_detinfopembersih.text = pembersih.info
        tv_detpembersihharga.text = pembersih.price
        tv_detpembersihnama.text = pembersih.name
        
        dialog = Dialog(ctx)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layoutInflater.inflate(R.layout.img_layout, null))
        imageView = dialog.findViewById(R.id.img_detail)
        val close : ImageView = dialog.findViewById(R.id.close_image)
        Glide.with(ctx).load(pembersih.image).into(imageView)

        img_detpembersih.setOnClickListener {
            dialog.show()
        }
        close.setOnClickListener {
            dialog.dismiss()
        }
    }
}
