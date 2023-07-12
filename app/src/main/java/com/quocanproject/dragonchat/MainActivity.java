package com.quocanproject.dragonchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.quocanproject.dragonchat.utils.FirebaseUtil;

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
        getFCMToken();
    }

    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    String token = task.getResult();
                    FirebaseUtil.currentUserDetails().update("fcmToken", token);
                    Log.d("MYTOKEN", token);
                }
            }
        });
    }

    private void initFragment() {
        fragProfile = new FragProfile();
        fragHome = new FragHome();
    }
}