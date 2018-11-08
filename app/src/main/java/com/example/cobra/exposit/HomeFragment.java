package com.example.cobra.exposit;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;


public class HomeFragment extends Fragment {
    private Button mAddButton;
    private DatabaseReference mNotes;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        mNotes = FirebaseDatabase.getInstance().getReference("Notes");
        mAddButton=homeView.findViewById(R.id.home_add);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //open home fragment
                AddFragment addFragment =new AddFragment();
                FragmentManager  manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.content_frame,addFragment).commit();
            }
        });
        return homeView;
    }

}
