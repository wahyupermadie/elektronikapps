package com.example.wahyupermadi.pembersihac.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.wahyupermadi.pembersihac.R
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

class SplashScreen : AppCompatActivity() {
    var firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null){
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("id",firebaseAuth.currentUser!!.uid)
                startActivity(intent)
            }, 5000)
        }else{
            Handler().postDelayed({
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }, 5000)
        }
    }
}
