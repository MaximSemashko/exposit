package com.example.cobra.exposit

import android.app.ProgressDialog
import android.content.Intent
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.util.HashMap

class RegistrationActivity : AppCompatActivity() {

    private var mName: TextInputLayout? = null
    private var mSurname: TextInputLayout? = null
    private var mAge: TextInputLayout? = null
    private var mSex: Spinner? = null
    private var mPassword: TextInputLayout? = null
    private var mEmail: TextInputLayout? = null
    private var mSignUpButton: Button? = null
    private var mRegProgress: ProgressDialog? = null
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        mName = findViewById(R.id.name_field)
        mSurname = findViewById(R.id.surname_field)
        mAge = findViewById(R.id.age_field)
        mSex = findViewById(R.id.sex_field)
        mPassword = findViewById(R.id.password_field)
        mEmail = findViewById(R.id.email_field)
        mAuth = FirebaseAuth.getInstance()
        mSignUpButton = findViewById(R.id.registration_button)
        mRegProgress = ProgressDialog(this)

        mSignUpButton!!.setOnClickListener(View.OnClickListener {
            val name = mName!!.editText!!.text.toString()
            val surname = mSurname!!.editText!!.text.toString()
            val age = mAge!!.editText!!.text.toString()
            val sex = mSex!!.selectedItem.toString()
            val email = mEmail!!.editText!!.text.toString()
            val password = mPassword!!.editText!!.text.toString()

            //Check fields
            if (name == "" || surname == "" || age == "" || sex == "" || email == "" || password == "") {
                Toast.makeText(this@RegistrationActivity, "Error: Empty field/fields", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this@RegistrationActivity, "Password is too short", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            //method to add
            mRegProgress!!.setTitle("Users registration")
            mRegProgress!!.setMessage("Please wait while we make your account...")
            mRegProgress!!.setCanceledOnTouchOutside(false)
            mRegProgress!!.show()
            register_user(name, surname, age, sex, email, password)
        })

    }

    private fun register_user(name: String, surname: String, age: String, sex: String, email: String, password: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        //Check for root
                        val current_user = FirebaseAuth.getInstance().currentUser
                        val uid = current_user!!.uid


                        //Add to Realtime db
                        mDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(uid)
                        val userMap = HashMap<String, String>()
                        userMap["Name"] = name
                        userMap["Surname"] = surname
                        userMap["Age"] = age
                        userMap["Sex"] = sex
                        userMap["Email"] = email
                        userMap["Password"] = password
                        mDatabase!!.setValue(userMap).addOnCompleteListener { task ->
                            //Go to main page
                            if (task.isSuccessful) {
                                mRegProgress!!.dismiss()
                                val mainIntent = Intent(this@RegistrationActivity, MainActivity::class.java)
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(mainIntent)
                                finish()
                            }
                        }
                    } else {
                        mRegProgress!!.hide()
                        Toast.makeText(this@RegistrationActivity, "Cannot sign up..", Toast.LENGTH_SHORT).show()
                    }
                }

    }
}