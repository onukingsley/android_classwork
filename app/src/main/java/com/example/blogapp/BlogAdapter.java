package com.example.blogapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BlogAdapter extends ArrayAdapter<BlogModel> {
    ArrayList<BlogModel> blogModel;
    Context context;
    int num;
    BlogModel blogmodel;


    public BlogAdapter(@NonNull Context context, ArrayList<BlogModel> blogModel, int num) {
        super(context,0 , blogModel);
        this.blogModel = blogModel;
        this.context = context;
        this.num = num;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
          View view;
        if (num == 1){
            view = LayoutInflater.from(context).inflate(R.layout.single_blog,parent,false);
            ImageView imageView = view.findViewById(R.id.image);
            TextView title = view.findViewById(R.id.title);
            TextView content = view.findViewById(R.id.content);
            TextView author = view.findViewById(R.id.author);

            title.setText(blogModel.get(position).title);
            content.setText(blogModel.get(position).getContent());
            getusername(blogModel.get(position).getUser_id(),author);
            Picasso.with(getContext()).load(blogModel.get(position).getImage()).into(imageView);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.single_post,parent,false);
            ImageView imageView = view.findViewById(R.id.image);
            TextView title = view.findViewById(R.id.title);
            TextView content = view.findViewById(R.id.content);
            ImageView edit = view.findViewById(R.id.edit);
            ImageView delete = view.findViewById(R.id.delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletepost(blogModel.get(position).getBlog_id());
                   /* Toast.makeText(view.getContext(), "your post has been deleted succesfully", Toast.LENGTH_SHORT).show();*/
                    remove(blogModel.get(position));

                }
            });



            title.setText(blogModel.get(position).getTitle());
            content.setText(blogModel.get(position).getContent());

            Picasso.with(getContext()).load(blogModel.get(position).getImage()).into(imageView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context,DisplayBlog.class);
                    i.putExtra("BlogModel", blogModel.get(position));
                    context.startActivity(i);
                }
            });

           /*assignment
           * do the two button (edit and delete)
           * edit should carry you to the next intent that fetches the data and sends it back to the database*/
        }

        return view;
    }
    public void getusername(String id , TextView view){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    view.setText(documentSnapshot.getString("username"));
                }
            }
        });
    }

    public void deletepost(String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("blog").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        documentSnapshot.getReference().delete();
                        Toast.makeText(context, "successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "error: "+ e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
