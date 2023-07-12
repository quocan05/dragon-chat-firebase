package com.quocanproject.dragonchat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.quocanproject.dragonchat.model.User;
import com.quocanproject.dragonchat.utils.AndroidUtil;
import com.quocanproject.dragonchat.utils.FirebaseUtil;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class FragProfile extends Fragment {


    EditText edtUsername, edtphoneNumber;
    TextView tvSignout;
    Button btnUpdateProfile;

    User currentUser;

    ImageView imgProfile;

    ActivityResultLauncher<Intent> pickImgLauncher;
    Uri selectedImgUri;

    public FragProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickImgLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data != null && data.getData() != null){
                            selectedImgUri =  data.getData();
                            AndroidUtil.setProfilePicture(getContext(), selectedImgUri, imgProfile);
                        }
                    }
                });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_profile,container, false);
        initUI(view);
        getUserData();

        setProfilePicture();

        btnUpdateProfile.setOnClickListener(view1 -> {
            String newUsername = edtUsername.getText().toString().trim();
            if (newUsername.isEmpty() || newUsername.length() < 5){
                edtUsername.setError("Must be least 5 characters !");
                return;
            }
            else {
                currentUser.setUsername(newUsername);
                if(selectedImgUri != null){
                    FirebaseUtil.getCurrentProfilePictureStorageRef().putFile(selectedImgUri)
                            .addOnCompleteListener(task -> {
                                updateToFireStore();
                            });
                } else {
                    updateToFireStore();
                }

            }
        });





        signOutUser();
        return view;
    }

    private void setProfilePicture() {
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(FragProfile.this).cropSquare().compress(1080).maxResultSize(1080, 1080)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                pickImgLauncher.launch(intent);

                                return null;
                            }
                        });
            }
        });
    }

    private void signOutUser() {
        tvSignout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseUtil.signOut();
                        Intent intent = new Intent(getContext(), SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
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

        FirebaseUtil.getCurrentProfilePictureStorageRef().getDownloadUrl()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Uri uri = task.getResult();
                                AndroidUtil.setProfilePicture(getContext(), uri, imgProfile);
                            }
                        });

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
        imgProfile = view.findViewById(R.id.imgFragmentProfile);
    }
}