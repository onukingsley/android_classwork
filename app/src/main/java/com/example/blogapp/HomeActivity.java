package com.example.blogapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    FloatingActionButton fab;
    private FirebaseAuth mAuth;
    FrameLayout frameLayout;
    BottomNavigationView nav;
    HomeFragment homefrag = new HomeFragment();
    PostFragment postfrag = new PostFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fab = findViewById(R.id.fab);
        mAuth = FirebaseAuth.getInstance();

        nav = findViewById(R.id.btmnav);
        frameLayout = findViewById(R.id.containernav
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.containernav,homefrag,"fragment").commit();

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.feeds){
                    getSupportFragmentManager().beginTransaction().replace(R.id.containernav,homefrag).commit();

                }
                if (item.getItemId() == R.id.my_posts){
                    getSupportFragmentManager().beginTransaction().replace(R.id.containernav,postfrag).commit();

                }

                return false;
            }

        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentuser = mAuth.getCurrentUser();

                if (currentuser == null){
                    Intent i = new Intent(HomeActivity.this,Signin.class);
                    startActivity(i);
                }
                else{
                    Intent  i = new Intent(HomeActivity.this,NewPost.class);
                    startActivity(i);
                }

            }
        });
    }
}