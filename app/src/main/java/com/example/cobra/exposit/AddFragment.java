package com.example.cobra.exposit;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AddFragment extends Fragment {
    private TextInputLayout mText;
    private Button mAddButton;
    private DatabaseReference mDatabase;
    private ProgressDialog mAddProgress;
    public AddFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View addView = inflater.inflate(R.layout.fragment_add, container, false);
       mText=addView.findViewById(R.id.the_note);
       mAddButton=addView.findViewById(R.id.add_note_button);
       mAddProgress=new ProgressDialog(getContext());

        //Add notes to db
       mAddButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String note = mText.getEditText().getText().toString();
               if(!TextUtils.isEmpty(note)){
                   mAddProgress.setTitle("Adding note");
                   mAddProgress.setMessage("Sending note...");
                   mAddProgress.setCanceledOnTouchOutside(false);
                   mAddProgress.show();
                   addNote(note);
               }
               else{
                   Toast.makeText(getContext(),"Check text field",Toast.LENGTH_SHORT).show();
               }

           }
       });
       return addView;
    }

    private void addNote(String note) {
        //get current user
        FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=current_user.getUid();

        //Add to Realtime db
        mDatabase = FirebaseDatabase.getInstance().getReference("Notes").child(uid);

        //get note id
        String noteId= mDatabase.push().getKey();

        //get date
        DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd");
        Date date = new Date();
        String currentDate =  dateFormat.format(date);

        // add to db
        Notes notes = new Notes(note,currentDate,uid);

        mDatabase.child(noteId).setValue(notes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    mAddProgress.dismiss();
                    mText.getEditText().setText("");
                    Toast.makeText(getContext(), "Note successfully added", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAddProgress.hide();
                    Toast.makeText(getContext(), "Something is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
