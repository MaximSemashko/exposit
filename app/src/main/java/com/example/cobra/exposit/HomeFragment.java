package com.example.cobra.exposit;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView mNotesList;
    private DatabaseReference mMyRef;
    private FirebaseDatabase mDatabase;

    ArrayList<String> notes= new ArrayList<>();

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        mNotesList=homeView.findViewById(R.id.notes_list);

        FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=current_user.getUid();
        mDatabase=FirebaseDatabase.getInstance();
        mMyRef=mDatabase.getReference().child("Notes").child(uid);

        mMyRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String note=dataSnapshot.getValue(String.class);
                notes.add(note);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return homeView;
    }

}
