package com.example.wahyupermadi.pembersihac

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_verify.*
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.SECONDS
import com.google.firebase.auth.PhoneAuthCredential
import android.widget.Toast
import com.google.firebase.FirebaseException
import android.content.Intent
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.text.TextUtils
import com.example.wahyupermadi.pembersihac.model.User
import com.example.wahyupermadi.pembersihac.view.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class VerifyActivity : AppCompatActivity() {
    var firebaseAuth = FirebaseAuth.getInstance()
    private var key: String? = null
    var verificationId : String = ""
    private var mDatabase: DatabaseReference? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mDatabase = mFirebaseDatabase!!.getReference("user")

        val phone = intent.getStringExtra("phone")

        sendVerificationCode(phone)

    }

    private fun sendVerificationCode(number: String) {
        progressbar.setVisibility(View.VISIBLE)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        )

    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                    override fun onComplete(p0: Task<AuthResult>) {
                        val name = intent.getStringExtra("name")
                        val email = intent.getStringExtra("email")
                        val uid = intent.getStringExtra("id")
                        val phone = intent.getStringExtra("phone")
                        val password = intent.getStringExtra("password")

                        signup(name, email, password, phone)
                    }
                })
    }

    private fun signup(name: String, email: String, password: String, phone: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult>{
                    override fun onComplete(task: Task<AuthResult>) {
                        if(task.isSuccessful){
                            val userUid = firebaseAuth.currentUser?.uid
                            val user = User("",name, email, phone, userUid,"","","")
                            mDatabase?.child(userUid!!)?.setValue(user)
                            progressbar.visibility = View.GONE

                            startActivity<MainActivity>()
                            finish()
//                            val intent = Intent(ctx, MainActivity::class.java)
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            startActivity(intent)
                        }else{
                            toast("register error")
                        }
                    }
                })
    }
    private val mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onCodeSent(s: String?, forceResendingToken: PhoneAuthProvider.ForceResendingToken?) {
            super.onCodeSent(s, forceResendingToken)
            verificationId = s!!
        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            val code = phoneAuthCredential.smsCode
            if (code != null) {
                editTextCode.setText(code)
                verifyCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
        }
    }
}
