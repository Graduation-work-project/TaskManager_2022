package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.myapplication.AlarmUi.AlarmActivity;
import com.example.myapplication.Community_ui.SetUpActivity;
import com.example.myapplication.Diary.DiaryList;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class Community_button extends AppCompatActivity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_button);

        ImageView sns_gif = (ImageView)findViewById(R.id.imageView7);
        Glide.with(this).load(R.raw.sns).into(sns_gif);

        ImageView diary_gif = (ImageView)findViewById(R.id.imageView6);
        Glide.with(this).load(R.raw.diary).into(diary_gif);

        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setItemIconTintList(null);
        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.community);

        //perform itemselectedlistener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.todolist:
                        startActivity(new Intent(getApplicationContext()
                                , ToDoList.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.community:

                        return true;
                    case R.id.calendar:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0,0);

                        return true;
                    case R.id.alarm:
                        startActivity(new Intent(getApplicationContext()
                                , AlarmActivity.class));
                        overridePendingTransition(0,0);

                        return true;
                    case R.id.info:
                        startActivity(new Intent(getApplicationContext()
                                , SetUpActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        Button btn_community = findViewById(R.id.btn_community);
        btn_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Community_button.this, Community.class);
                startActivity(intent);
            }
        });

        Button btn_diary = findViewById(R.id.btn_diary);
        btn_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Community_button.this, DiaryList.class);
                startActivity(intent);
            }
        });

        //cardView2.setClickable(true);
        CardView cardView2 = findViewById(R.id.cardView2);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Community_button.this, Community.class);
                startActivity(intent);
            }
        });

        CardView cardView = findViewById(R.id.cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Community_button.this, DiaryList.class);
                startActivity(intent);
            }
        });
    }
}
