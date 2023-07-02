package com.quocanproject.dragonchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton btnSearchMain;

    FragProfile fragProfile;
    FragHome fragHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
        initUI();
        addAction();
    }

    private void addAction() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.homeMenu){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutMain, fragHome).commit();

                }
                if(item.getItemId() == R.id.profileMenu){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutMain, fragProfile).commit();

                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.homeMenu);

        btnSearchMain.setOnClickListener(view -> {
            //search chat
            Intent intent = new Intent(MainActivity.this, SearchChatActivity.class);
            startActivity(intent);
        });
    }

    private void initUI() {
        bottomNavigationView = findViewById(R.id.bottomNav);
        btnSearchMain = findViewById(R.id.btnSearchMain);

    }

    private void initFragment() {
        fragProfile = new FragProfile();
        fragHome = new FragHome();
    }
}