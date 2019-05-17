package com.example.cobra.exposit


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import java.nio.FloatBuffer
import java.util.ArrayList


class HomeFragment : Fragment() {
    private var mAddButton: Button? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mNotesRecycler: RecyclerView? = null


    override fun onStart() {
        super.onStart()

        val options = FirebaseRecyclerOptions.Builder<Notes>()
                .setQuery(mDatabaseReference!!, Notes::class.java)
                .build()
        val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Notes, notesViewHolder>(options) {
            override fun onBindViewHolder(holder: notesViewHolder, position: Int, model: Notes) {
                holder.date_text.text = model.date
                holder.note_text.text = model.name

            }

            override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): notesViewHolder {
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.single_note_layout,
                        viewGroup, false)
                return notesViewHolder(view)
            }
        }
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        if (user != null) {
            mNotesRecycler!!.adapter = firebaseRecyclerAdapter
            firebaseRecyclerAdapter.startListening()
            firebaseRecyclerAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val homeView = inflater.inflate(R.layout.fragment_home, container, false)


        mAddButton = homeView.findViewById(R.id.home_add)
        mNotesRecycler = homeView.findViewById(R.id.notes_recycler)
        mNotesRecycler!!.setHasFixedSize(true)
        mNotesRecycler!!.layoutManager = LinearLayoutManager(context)

        //get current user
        val current_user = FirebaseAuth.getInstance().currentUser
        if (current_user != null) {
            val uid = current_user.uid
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(uid)
        }


        //go to add fragment
        mAddButton!!.setOnClickListener {
            //open add fragment
            val addFragment = AddFragment()
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(R.id.content_frame, addFragment).commit()
        }

        return homeView
    }


    inner class notesViewHolder(internal var mView: View) : RecyclerView.ViewHolder(mView) {
        internal var note_text: TextView
        internal var date_text: TextView

        init {
            note_text = mView.findViewById(R.id.single_note_text)
            date_text = mView.findViewById(R.id.single_date)


        }
    }

}
