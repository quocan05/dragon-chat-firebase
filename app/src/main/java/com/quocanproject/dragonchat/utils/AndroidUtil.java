package com.quocanproject.dragonchat.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.quocanproject.dragonchat.model.User;

public class AndroidUtil {
    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void passUserDataAsIntent(Intent intent, User user){
        intent.putExtra("username", user.getUsername());
        intent.putExtra("phonenumber", user.getPhone());
        intent.putExtra("uid", user.getuID());
        intent.putExtra("fcmToken", user.getFcmToken());
    }

    public static User getUserFromIntent(Intent intent){
        User user = new User();
        user.setUsername(intent.getStringExtra("username"));
        user.setPhone(intent.getStringExtra("phonenumber"));
        user.setuID(intent.getStringExtra("uid"));
        user.setFcmToken(intent.getStringExtra("fcmToken"));
        return user;
    }

    public static void setProfilePicture(Context context, Uri uri, ImageView img){
        Glide.with(context).load(uri).apply(RequestOptions.circleCropTransform()).into(img);
    }
}
