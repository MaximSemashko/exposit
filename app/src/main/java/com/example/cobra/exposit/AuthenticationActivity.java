package com.example.cobra.exposit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationActivity extends AppCompatActivity {

    private Button mSignInBtn;
    private Button mSignUpBtn;
    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog mLogDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mAuth=FirebaseAuth.getInstance();
        mSignInBtn=findViewById(R.id.sign_in_button);
        mSignUpBtn=findViewById(R.id.sign_up_button);
        mLoginEmail=findViewById(R.id.login_email_field);
        mLoginPassword=findViewById(R.id.login_password_field);
        mLogDialog=new ProgressDialog(this);

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();
                if(email.equals("")||password.equals("")){
                    Toast.makeText(AuthenticationActivity.this,"Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                    mLogDialog.setTitle("Login");
                    mLogDialog.setMessage("Wait until you log in");
                    mLogDialog.setCanceledOnTouchOutside(false);
                    mLogDialog.show();
                    loginUser(email,password);
            }
        });

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registration= new Intent(AuthenticationActivity.this,RegistrationActivity.class);
                startActivity(registration);
                finish();
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mLogDialog.dismiss();
                    Intent mainIntent = new Intent(AuthenticationActivity.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }else{
                    mLogDialog.hide();
                    Toast.makeText(AuthenticationActivity.this,"Cannot sign in..",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
