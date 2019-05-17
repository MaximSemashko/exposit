package com.example.cobra.exposit

import android.app.ProgressDialog
import android.content.Intent
import android.icu.text.UnicodeSetSpanner
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class AuthenticationActivity : AppCompatActivity() {

    private var mSignInBtn: Button? = null
    private var mSignUpBtn: Button? = null
    private var mLoginEmail: TextInputLayout? = null
    private var mLoginPassword: TextInputLayout? = null
    private var mAuth: FirebaseAuth? = null
    private var mLogDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        mAuth = FirebaseAuth.getInstance()
        mSignInBtn = findViewById(R.id.sign_in_button)
        mSignUpBtn = findViewById(R.id.sign_up_button)
        mLoginEmail = findViewById(R.id.login_email_field)
        mLoginPassword = findViewById(R.id.login_password_field)
        mLogDialog = ProgressDialog(this)

        mSignInBtn!!.setOnClickListener(View.OnClickListener {
            val email = mLoginEmail!!.editText!!.text.toString()
            val password = mLoginPassword!!.editText!!.text.toString()
            if (email == "" || password == "") {
                Toast.makeText(this@AuthenticationActivity, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            mLogDialog!!.setTitle("Login")
            mLogDialog!!.setMessage("Wait until you log in")
            mLogDialog!!.setCanceledOnTouchOutside(false)
            mLogDialog!!.show()
            loginUser(email, password)
        })

        mSignUpBtn!!.setOnClickListener {
            val registration = Intent(this@AuthenticationActivity, RegistrationActivity::class.java)
            startActivity(registration)
            finish()
        }
    }

    private fun loginUser(email: String, password: String) {
        mAuth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mLogDialog!!.dismiss()
                val mainIntent = Intent(this@AuthenticationActivity, MainActivity::class.java)
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(mainIntent)
                finish()
            } else {
                mLogDialog!!.hide()
                Toast.makeText(this@AuthenticationActivity, "Cannot sign in..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
