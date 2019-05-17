package com.example.cobra.exposit


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private var mName: TextView? = null
    private var mSurname: TextView? = null
    private var mAge: TextView? = null
    private var mSex: TextView? = null
    private var mEmail: TextView? = null
    private var mUserDatabase: DatabaseReference? = null
    private var mCurrentUser: FirebaseUser? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val mMainView = inflater.inflate(R.layout.fragment_profile, container, false)

        mName = mMainView.findViewById(R.id.name_text)
        mSurname = mMainView.findViewById(R.id.surname_text)
        mAge = mMainView.findViewById(R.id.age_text)
        mSex = mMainView.findViewById(R.id.sex_text)
        mEmail = mMainView.findViewById(R.id.email_text)


        mCurrentUser = FirebaseAuth.getInstance().currentUser

        val current_uid = mCurrentUser!!.uid



        mUserDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(current_uid)
        mUserDatabase!!.keepSynced(true)
        mUserDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.child("Name").value!!.toString()
                val surname = dataSnapshot.child("Surname").value!!.toString()
                val age = dataSnapshot.child("Age").value!!.toString()
                val sex = dataSnapshot.child("Sex").value!!.toString()
                val email = dataSnapshot.child("Email").value!!.toString()

                mName!!.text = name
                mSurname!!.text = surname
                mAge!!.text = age
                mSex!!.text = sex
                mEmail!!.text = email
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        return mMainView
    }

}// Required empty public constructor
