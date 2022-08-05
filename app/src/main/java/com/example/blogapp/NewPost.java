package com.example.blogapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.DialogCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class NewPost extends AppCompatActivity {

    ImageButton imageview;
    EditText title, content;
    TextView text, homepage;
    Button submitBtn;
    int resultcod = 200;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri selectedImageuri;

    
    String blogtitle, blogimageurl, blogcontent;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        imageview = findViewById(R.id.ima);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        text = findViewById(R.id.text);
        homepage = findViewById(R.id.homepage);
        submitBtn = findViewById(R.id.submit);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        progressDialog = new ProgressDialog(this);

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagechooser();
            }
        });
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewPost.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titletxt = title.getText().toString();
                String contenttxt = content.getText().toString();

                if (titletxt.trim().length() < 10){
                    title.setError("Title must be greater than 10 characters");

                }else if (contenttxt.trim().length()<10) {
                    content.setError("content must be greater than 10 characters");
                }else if(selectedImageuri == null) {
                    text.setText("please select an image");
                }
                else {
                    
                    blogtitle = titletxt;
                    blogcontent = contenttxt;
                    progressDialog.show();

                    uploadImage();
                }
            }
        });


    }


    public void imagechooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select image"),resultcod);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK ){
            if (requestCode == resultcod){
                selectedImageuri = data.getData();
            }

            if (null != selectedImageuri){
                imageview.setImageURI(selectedImageuri);
            }else{
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(this, "error student", Toast.LENGTH_SHORT).show();

        }

    }

    public void uploadImage(){
        StorageReference ref = storageReference
                .child("images/*" + UUID.randomUUID().toString());
        ref.putFile(selectedImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        blogimageurl = task.getResult().toString();
                        
                        postblog();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(NewPost.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(NewPost.this, "Image upload Failed " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        
    }
    public void postblog(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser userCredential = mAuth.getCurrentUser();
        Map<String, Object> blog = new HashMap<>();
        blog.put("title", blogtitle);
        blog.put("content", blogcontent);
        blog.put("image", blogimageurl);
        blog.put("user_id", userCredential.getUid());
        
        db.collection("blog")
                .add(blog)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.dismiss();
                        Toast.makeText(NewPost.this, "Post successfully created ", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder builder  = new AlertDialog.Builder(NewPost.this);
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(NewPost.this,  HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                title.setText("");
                                content.setText("");

                            }
                        }).setTitle("Do you want to creat another post");
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(NewPost.this, "Error in field "+e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        
    }
    
}
