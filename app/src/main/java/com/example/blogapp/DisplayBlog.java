package com.example.blogapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DisplayBlog extends AppCompatActivity {
ImageView imageview;
TextView title, content;
BlogModel blogModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_blog);
        imageview = findViewById(R.id.image);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        blogModel =(BlogModel) getIntent().getSerializableExtra("BlogModel");
        Picasso.with(this).load(blogModel.getImage()).into(imageview);
        title.setText(blogModel.getTitle());
        content.setText(blogModel.getContent());

    }
}