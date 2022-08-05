package com.example.blogapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signin extends AppCompatActivity {
        EditText email,password;
        Button signBtn;
        TextView signuptxt;
        private FirebaseAuth mAuth;
        ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        email = findViewById(R.id.email);
        password = findViewById(R.id.Passwd);
        signBtn = findViewById(R.id.signin);
        signuptxt = findViewById(R.id.signup);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);


        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailtxt = email.getText().toString();
                String passwordtxt = password.getText().toString();

                if(!emailtxt.trim().isEmpty() && !passwordtxt.trim().isEmpty()){
                    login(emailtxt,passwordtxt);
                }
            }
        });
        signuptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Signin.this,SignUp.class);
                startActivity(i);

            }
        });
    }

    public void login(String email, String password){
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String id = user.getUid();
                            Intent i = new Intent(Signin.this, NewPost.class);
                            i.putExtra("id", id);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(Signin.this, "wrong email and password", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}