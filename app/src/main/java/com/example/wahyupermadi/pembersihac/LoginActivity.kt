package com.example.wahyupermadi.pembersihac

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.wahyupermadi.pembersihac.view.MainActivity
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.R.attr.password
import android.util.Log


class LoginActivity : AppCompatActivity() {
    var firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        link_signup.setOnClickListener{
            startActivity<RegisterActivity>()
        }

        btn_login.setOnClickListener{
            signIn()
        }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null){
            startActivity<MainActivity>("id" to "${firebaseAuth.currentUser!!.uid}")
        }
    }

    fun signIn() {
        val email = input_email.text.toString()
        val password = input_password.text.toString()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, object : OnCompleteListener<AuthResult>{
            override fun onComplete(task: Task<AuthResult>) {
                if(task.isSuccessful){
                    startActivity<MainActivity>("id" to "${firebaseAuth.currentUser?.email}")
                }else{
                    toast("Username atau password salah")
                }
            }
        })
    }
}
