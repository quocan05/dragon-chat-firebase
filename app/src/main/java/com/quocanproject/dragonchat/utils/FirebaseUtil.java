package com.quocanproject.dragonchat.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

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

    public static CollectionReference allChatRoomCollectionRef(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherUserFromChatRoom(List<String> uIDs){
        if(uIDs.get(0).equals(FirebaseUtil.currentUserID())){
            return allUserCollectionRef().document(uIDs.get(1));
        }
        else {
            return allUserCollectionRef().document(uIDs.get(0));
        }
    }


    public static String TimestampToString(Timestamp time){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(time.toDate());
    }

    public static void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference getCurrentProfilePictureStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(FirebaseUtil.currentUserID());
    }
    public static StorageReference getOtherUserProfilePictureStorageRef(String otherUserId){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(otherUserId);
    }
}
