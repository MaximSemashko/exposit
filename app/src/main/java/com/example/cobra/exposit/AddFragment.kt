package com.example.cobra.exposit


import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap

class AddFragment : Fragment() {
    private var mText: TextInputLayout? = null
    private var mAddButton: Button? = null
    private var mDatabase: DatabaseReference? = null
    private var mAddProgress: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val addView = inflater.inflate(R.layout.fragment_add, container, false)
        mText = addView.findViewById(R.id.the_note)
        mAddButton = addView.findViewById(R.id.add_note_button)
        mAddProgress = ProgressDialog(context)

        //Add notes to db
        mAddButton!!.setOnClickListener {
            val note = mText!!.editText!!.text.toString()
            if (!TextUtils.isEmpty(note)) {
                mAddProgress!!.setTitle("Adding note")
                mAddProgress!!.setMessage("Sending note...")
                mAddProgress!!.setCanceledOnTouchOutside(false)
                mAddProgress!!.show()
                addNote(note)
            } else {
                Toast.makeText(context, "Check text field", Toast.LENGTH_SHORT).show()
            }
        }
        return addView
    }

    private fun addNote(note: String) {
        //get current user
        val current_user = FirebaseAuth.getInstance().currentUser
        val uid = current_user!!.uid

        //Add to Realtime db
        mDatabase = FirebaseDatabase.getInstance().getReference("Notes").child(uid)

        //get note id
        val noteId = mDatabase!!.push().key

        //get date
        val dateFormat = SimpleDateFormat("yyy/MM/dd")
        val date = Date()
        val currentDate = dateFormat.format(date)

        // add to db
        val notes = Notes(note, currentDate, uid)

        mDatabase!!.child(noteId).setValue(notes).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mAddProgress!!.dismiss()
                mText!!.editText!!.setText("")
                Toast.makeText(context, "Note successfully added", Toast.LENGTH_SHORT).show()
            } else {
                mAddProgress!!.hide()
                Toast.makeText(context, "Something is wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
