package com.quocanproject.dragonchat.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {

    public static String currentUserID(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn(){
        if(currentUserID() != null){
            return true;
        }
        return false;
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("user").document(currentUserID());
    }

    public static CollectionReference allUserCollectionRef(){
        return FirebaseFirestore.getInstance().collection("user");
    }

    public static DocumentReference getChatRoomRef(String chatRoomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId);
    }

    public static CollectionReference getChatRoomMessageRef(String chatRoomID){
        return getChatRoomRef(chatRoomID).collection("chat");
    }

    public static String getChatRoomId(String uid1, String uid2){
        if(uid1.hashCode()<uid2.hashCode()){
            return uid1+"_"+uid2;
        }else {
            return uid2+"_"+uid1;
        }
    }
}
