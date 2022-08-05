package com.example.blogapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText email,password,username;
    Button signupBtn;
    TextView signintxt;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email);
        password = findViewById(R.id.Password);
        signupBtn = findViewById(R.id.signup);
        username = findViewById(R.id.username);
        signintxt = findViewById(R.id.signin);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);



        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailtxt = email.getText().toString();
                String passwordtxt = password.getText().toString();
                String usernametxt = username.getText().toString();

                if(emailtxt.trim().isEmpty() || usernametxt.trim().isEmpty() || passwordtxt.trim().isEmpty()){
                    Toast.makeText(SignUp.this, "All is required", Toast.LENGTH_SHORT).show();
                }
                signup(emailtxt,passwordtxt,usernametxt);
            }
        });

        signintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp.this,Signin.class);
                startActivity(i);
            }
        });
    }

    public void signup (String email, String password,String username){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser usercredential = mAuth.getCurrentUser();
                    String id = usercredential.getUid();

                    Map<String,Object> user = new HashMap<>();
                    user.put("username",username);
                    user.put("password",password);
                    user.put("email",email);
                    user.put("id",id);
                    register(user);
                    progressDialog.dismiss();
                }
                else{
                    Toast.makeText(SignUp.this, "A problem has occured "+ task.getException().toString(), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public void register(Map<String, Object> user){
        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SignUp.this, "SUCCESSFUL", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignUp.this,NewPost.class);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, "Error in field "+e , Toast.LENGTH_SHORT).show();
                        Log.i("erroor", "error adding document");
                    }
                });
    }

}