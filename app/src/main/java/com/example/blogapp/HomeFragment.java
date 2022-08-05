package com.example.blogapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


ArrayList<BlogModel> blogModel;
BlogAdapter blogAdapter;
FirebaseFirestore db = FirebaseFirestore.getInstance();

    public HomeFragment() {

    }

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blogModel = new ArrayList<BlogModel>();
        blogAdapter = new BlogAdapter(getContext(),blogModel,1);
        blogAdapter.notifyDataSetChanged();
        fetchpost();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        listView = view.findViewById(R.id.list_view);
        blogAdapter = new BlogAdapter(view.getContext(),blogModel , 1);
        listView.setAdapter(blogAdapter);
        blogAdapter.notifyDataSetChanged();
        return view;
    }
    public void fetchpost(){
        db.collection("blog")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                BlogModel blo = new BlogModel(document.getId(), document.getString("title"),document.getString("content"),document.getString("user_id"),document.getString("image"));
                                blogModel.add(blo);
                                blogAdapter.notifyDataSetChanged();
                            }
                        }else{
                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
