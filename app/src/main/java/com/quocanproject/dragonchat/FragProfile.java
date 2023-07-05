package com.quocanproject.dragonchat;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.quocanproject.dragonchat.model.User;
import com.quocanproject.dragonchat.utils.AndroidUtil;
import com.quocanproject.dragonchat.utils.FirebaseUtil;

public class FragProfile extends Fragment {


    EditText edtUsername, edtphoneNumber;
    TextView tvSignout;
    Button btnUpdateProfile;

    User currentUser;

    public FragProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_profile,container, false);
        initUI(view);
        getUserData();

        btnUpdateProfile.setOnClickListener(view1 -> {
            String newUsername = edtUsername.getText().toString().trim();
            if (newUsername.isEmpty() || newUsername.length() < 5){
                edtUsername.setError("Must be least 5 characters !");
                return;
            }
            else {
                currentUser.setUsername(newUsername);
                updateToFireStore();
            }
        });

        signOutUser();
        return view;
    }

    private void signOutUser() {
        tvSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUtil.signOut();
                Intent intent = new Intent(getContext(), SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void updateToFireStore() {
        FirebaseUtil.currentUserDetails().set(currentUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            AndroidUtil.showToast(getContext(), "Update username successfully!");
                        } else {
                            AndroidUtil.showToast(getContext(), "Update username failed, try again!");
                        }
                    }
                });
    }

    private void getUserData() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
           currentUser = task.getResult().toObject(User.class);
           edtUsername.setText(currentUser.getUsername());
           edtphoneNumber.setText(currentUser.getPhone());
        });
    }

    private void initUI(View view) {
        edtUsername = view.findViewById(R.id.edtUsernameProfile);
        edtphoneNumber = view.findViewById(R.id.phoneNumberProfile);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        tvSignout = view.findViewById(R.id.tvSignOut);
    }
}