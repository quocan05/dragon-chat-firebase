package com.quocanproject.dragonchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.quocanproject.dragonchat.model.User;
import com.quocanproject.dragonchat.utils.FirebaseUtil;

public class EnterUsername extends AppCompatActivity {

    EditText edtUsernameInput;
    Button btnLetMeIn;

    String phoneNumber;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_username);
        initUI();
        phoneNumber = getIntent().getExtras().getString("phone");

        getUsername();

        onClickLetMeIn();
    }

    private void onClickLetMeIn() {
        btnLetMeIn.setOnClickListener(view -> {
            setUsername();
        });
    }

    private void setUsername() {
        String username = edtUsernameInput.getText().toString().trim();
        if(username.isEmpty() || username.length() < 5){
            edtUsernameInput.setError("Username length should be least 5 characters !");
            return;
        } else {
            user = new User(phoneNumber, username, Timestamp.now());
        }

        FirebaseUtil.currentUserDetails().set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(EnterUsername.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

    }

    private void getUsername() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                user = task.getResult().toObject(User.class);
               if(user != null){
                   edtUsernameInput.setText(user.getUsername());
               }
            }
        });
    }

    private void initUI() {
        edtUsernameInput = findViewById(R.id.edtEnterUsername);
        btnLetMeIn = findViewById(R.id.btnLetMeIn);
    }
}