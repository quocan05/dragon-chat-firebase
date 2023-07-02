package com.quocanproject.dragonchat.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.quocanproject.dragonchat.model.User;

public class AndroidUtil {
    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void passUserDataAsIntent(Intent intent, User user){
        intent.putExtra("username", user.getUsername());
        intent.putExtra("phonenumber", user.getPhone());
        intent.putExtra("uid", user.getuID());
    }

    public static User getUserFromIntent(Intent intent){
        User user = new User();
        user.setUsername(intent.getStringExtra("username"));
        user.setPhone(intent.getStringExtra("phonenumber"));
        user.setuID(intent.getStringExtra("uid"));
        return user;
    }
}
