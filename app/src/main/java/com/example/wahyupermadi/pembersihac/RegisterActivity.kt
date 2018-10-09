package com.example.wahyupermadi.pembersihac

import android.R.attr.key
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.text.TextUtils
import android.util.Log
import com.example.wahyupermadi.pembersihac.model.User
import com.example.wahyupermadi.pembersihac.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class RegisterActivity : AppCompatActivity() {
    var firebaseAuth = FirebaseAuth.getInstance()
    private var key: String? = null
    private var mDatabase: DatabaseReference? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabase = mFirebaseDatabase!!.getReference("user")

        btn_register.setOnClickListener{
            register()
        }

        link_login.setOnClickListener {
            startActivity<LoginActivity>()
        }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null){
            startActivity<MainActivity>("id" to "${firebaseAuth.currentUser!!.uid}")
        }
    }
    private fun register() {
        val name = rg_nama.text.toString()
        val email = rg_email.text.toString()
        val pass = rg_password.text.toString()
        val telepon = rg_telepon.text.toString()

        insertProfile(name,email,pass,telepon)
    }
    private fun insertProfile(name : String, email : String, pass: String, phone : String) {
            startActivity<VerifyActivity>("name" to "${name}","email" to "${email}","password" to "${pass}","phone" to "${phone}")
    }
}
