package com.example.blogapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


 
public class PostFragment extends Fragment {
    ListView listView;
    ArrayList<BlogModel> blogmodels;
    BlogAdapter blogAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

 
    public PostFragment() {
        // Required empty public constructor
    }

   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blogmodels = new ArrayList<BlogModel>();
        blogAdapter = new BlogAdapter(getContext(),blogmodels,1);
        blogAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post,container,false);
        listView = view.findViewById(R.id.listcontainer);
        blogAdapter = new BlogAdapter(view.getContext(),blogmodels , 2);
        listView.setAdapter(blogAdapter);
        blogAdapter.notifyDataSetChanged();

        LinearLayout visitor = view.findViewById(R.id.visitor);
        Button login = view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),Signin.class);
                startActivity(i);
                
            }
        });

        if (user != null){
            visitor.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            fetchpost();
        }
        
        
        return view;
    }
    
    public void fetchpost(){
        db.collection("blog")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                String id = firebaseUser.getUid();
                                if (id.equals(documentSnapshot.getString("user_id"))){
                                    BlogModel blogModel = new BlogModel(documentSnapshot.getId(),documentSnapshot.getString("title")
                                            ,documentSnapshot.getString("content"),
                                            documentSnapshot.getString("user_id"),
                                            documentSnapshot.getString("image"));

                                    blogmodels.add(blogModel);
                                    blogAdapter.notifyDataSetChanged();
                                }
                            }
                        }else{
                            Toast.makeText(getContext(), "error fetching post as user hasnt posted anything", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}