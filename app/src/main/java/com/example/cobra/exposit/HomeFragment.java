package com.example.cobra.exposit;


import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
      private Button mAddButton;
      private DatabaseReference mDatabaseReference;
      private RecyclerView mNotesRecycler;


    public HomeFragment() {
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Notes> options =
                new FirebaseRecyclerOptions.Builder<Notes>()
                        .setQuery(mDatabaseReference, Notes.class)
                        .build();
        FirebaseRecyclerAdapter<Notes,notesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Notes, notesViewHolder>(options){
            @Override
            protected void onBindViewHolder(@NonNull notesViewHolder holder, int position, @NonNull Notes model) {
                holder.date_text.setText(model.getDate());
                holder.note_text.setText(model.getName());

            }

            @NonNull
            @Override
            public notesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_note_layout,
                        viewGroup,false);
                notesViewHolder notesViewHolder = new notesViewHolder(view);
                return  notesViewHolder;
            }
        };
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null) {
            mNotesRecycler.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.startListening();
            firebaseRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);


         mAddButton=homeView.findViewById(R.id.home_add);
         mNotesRecycler=homeView.findViewById(R.id.notes_recycler);
         mNotesRecycler.setHasFixedSize(true);
         mNotesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        //get current user
        FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
        if(current_user!=null) {
            String uid = current_user.getUid();
            mDatabaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(uid);
        }




        //go to add fragment
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //open add fragment
                AddFragment addFragment =new AddFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,addFragment).commit();
            }
        });

        return homeView;
    }


    public class notesViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView note_text, date_text;
        public notesViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            note_text = itemView.findViewById(R.id.single_note_text);
            date_text = itemView.findViewById(R.id.single_date);


        }
    }

}
