package com.example.cobra.exposit;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView mName;
    private TextView mSurname;
    private TextView mAge;
    private TextView mSex;
    private TextView mEmail;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View mMainView = inflater.inflate(R.layout.fragment_profile, container, false);

       mName=mMainView.findViewById(R.id.name_text);
       mSurname=mMainView.findViewById(R.id.surname_text);
       mAge=mMainView.findViewById(R.id.age_text);
       mSex=mMainView.findViewById(R.id.sex_text);
       mEmail=mMainView.findViewById(R.id.email_text);


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();



        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue().toString();
                String surname= dataSnapshot.child("Surname").getValue().toString();
                String age = dataSnapshot.child("Age").getValue().toString();
                String sex = dataSnapshot.child("Sex").getValue().toString();
                String email = dataSnapshot.child("Email").getValue().toString();

                mName.setText(name);
                mSurname.setText(surname);
                mAge.setText(age);
                mSex.setText(sex);
                mEmail.setText(email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       return mMainView;
    }

}
