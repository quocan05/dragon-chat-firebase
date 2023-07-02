package com.quocanproject.dragonchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.quocanproject.dragonchat.model.ChatRoom;
import com.quocanproject.dragonchat.model.User;
import com.quocanproject.dragonchat.utils.AndroidUtil;
import com.quocanproject.dragonchat.utils.FirebaseUtil;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {


    User userToChat;
    ChatRoom chatRoom;
    TextView tvUsernameToChat;
    EditText edtInputMsg;
    ImageButton btnSendMsg, btnBackChat;

    RecyclerView recyclerView;

    String chatRoomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userToChat = AndroidUtil.getUserFromIntent(getIntent());
        chatRoomID = FirebaseUtil.getChatRoomId(FirebaseUtil.currentUserID(), userToChat.getuID());
        initUI();

        sendMsg();

    }

    private void sendMsg() {
        btnSendMsg.setOnClickListener(view -> {
            String msg = edtInputMsg.getText().toString().trim();
            if(msg.isEmpty()){
                return;
            }
            sendToUser();
        });
    }

    private void sendToUser() {
    }

    private void initUI() {
        tvUsernameToChat = findViewById(R.id.tvUsernameTochat);
        btnSendMsg = findViewById(R.id.btnSendMsg);
        btnBackChat = findViewById(R.id.btnBackChat);
        edtInputMsg = findViewById(R.id.edtTypeMessage);
        recyclerView = findViewById(R.id.rcvChatLine);

        tvUsernameToChat.setText(userToChat.getUsername().toString().trim());
        btnBackChat.setOnClickListener(view -> {
           onBackPressed();
        });
        getOrCreateChatRoom();
    }

    private void getOrCreateChatRoom() {
        FirebaseUtil.getChatRoomRef(chatRoomID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                chatRoom = task.getResult().toObject(ChatRoom.class);
                if(chatRoom == null){
                    chatRoom = new ChatRoom(
                            chatRoomID,
                            Arrays.asList( FirebaseUtil.currentUserID(), userToChat.getuID()),
                            Timestamp.now(),
                            "");
                    FirebaseUtil.getChatRoomRef(chatRoomID).set(chatRoom);
                }
            }
        });
    }
}