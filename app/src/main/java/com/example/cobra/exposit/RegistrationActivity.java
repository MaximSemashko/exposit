package com.example.cobra.exposit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputLayout mName;
    private TextInputLayout mSurname;
    private TextInputLayout mAge;
    private TextInputLayout mSex;
    private TextInputLayout mPassword;
    private TextInputLayout mEmail;
    private Button mSignUpButton;
    private ProgressDialog mRegProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mName=findViewById(R.id.name_field);
        mSurname=findViewById(R.id.surname_field);
        mAge=findViewById(R.id.age_field);
        mSex=findViewById(R.id.sex_field);
        mPassword=findViewById(R.id.password_field);
        mEmail=findViewById(R.id.email_field);
        mAuth = FirebaseAuth.getInstance();
        mSignUpButton=findViewById(R.id.registration_button);
        mRegProgress = new ProgressDialog(this);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=mName.getEditText().getText().toString();
                String surname=mSurname.getEditText().getText().toString();
                String age=mAge.getEditText().getText().toString();
                String sex=mSex.getEditText().getText().toString();
                String email=mEmail.getEditText().getText().toString();
                String password=mPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(name)||!TextUtils.isEmpty(surname)||!TextUtils.isEmpty(age)||
                        !TextUtils.isEmpty(sex)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)){
                    //method to add
                    mRegProgress.setTitle("Users registration");
                    mRegProgress.setMessage("Please wait while we make your account...");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(name,surname,age,sex,email,password);
                }
            }
        });

    }

    private void register_user(final String name, final String surname,final String age, final String sex, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //Check for root element
                            FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                            String uid=current_user.getUid();


                            //Add to Realtime db
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            HashMap<String,String> userMap= new HashMap<>();
                            userMap.put("Name",name);
                            userMap.put("Surname",surname);
                            userMap.put("Age",age);
                            userMap.put("Sex",sex);
                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                     //Go to main page
                                    if(task.isSuccessful()) {
                                        mRegProgress.dismiss();
                                        Intent mainIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            mRegProgress.hide();
                            Toast.makeText(RegistrationActivity.this,"Cannot sign up..",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
