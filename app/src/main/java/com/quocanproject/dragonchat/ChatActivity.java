package com.quocanproject.dragonchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.quocanproject.dragonchat.adapter.ChatRecycleAdapter;
import com.quocanproject.dragonchat.model.ChatMessage;
import com.quocanproject.dragonchat.model.ChatRoom;
import com.quocanproject.dragonchat.model.User;
import com.quocanproject.dragonchat.utils.AndroidUtil;
import com.quocanproject.dragonchat.utils.FirebaseUtil;
import com.quocanproject.dragonchat.utils.KeyboardUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {


    User userToChat;
    ChatRoom chatRoom;
    TextView tvUsernameToChat;
    EditText edtInputMsg;
    ImageButton btnSendMsg, btnBackChat;

    RecyclerView recyclerView;
    ImageView imgProfileUserOnChat;
    ChatRecycleAdapter chatRecycleAdapter;

    String chatRoomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userToChat = AndroidUtil.getUserFromIntent(getIntent());
        chatRoomID = FirebaseUtil.getChatRoomId(FirebaseUtil.currentUserID(), userToChat.getuID());
        initUI();
        FirebaseUtil.getOtherUserProfilePictureStorageRef(userToChat.getuID()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePicture(this, uri, imgProfileUserOnChat);
                    }
                });
        sendMsg();

    }

    private void sendMsg() {
        btnSendMsg.setOnClickListener(view -> {
            String msg = edtInputMsg.getText().toString().trim();
            if(msg.isEmpty()){
                return;
            }
            sendToUser(msg);

            KeyboardUtil.hideKeyboard(this);

        });
    }

    private void sendToUser(String msg) {
        chatRoom.setLastMsgTimestamp(Timestamp.now());
        chatRoom.setLastMsgSenderID(FirebaseUtil.currentUserID());
        chatRoom.setLastMsg(msg);
        FirebaseUtil.getChatRoomRef(chatRoomID).set(chatRoom);

        //send msg here
        ChatMessage chatMessage = new ChatMessage(msg, FirebaseUtil.currentUserID(), Timestamp.now());
        FirebaseUtil.getChatRoomMessageRef(chatRoomID).add(chatMessage)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        edtInputMsg.setText("");
                        sendNotification(msg);
                    }
                });
    }

    private void sendNotification(String msg) {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    User currentUser = task.getResult().toObject(User.class);
                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject notificationObj = new JSONObject();
                        notificationObj.put("title", currentUser.getUsername());
                        notificationObj.put("body", msg);

                        JSONObject dataObj = new JSONObject();
                        dataObj.put("uID", currentUser.getuID());

                        jsonObject.put("notification", notificationObj);
                        jsonObject.put("data", dataObj);
                        jsonObject.put("to", userToChat.getFcmToken());

                        callAPI(jsonObject);
                    } catch (Exception e){

                    }
                }
            }
        });

    }

    void callAPI(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody requestBody = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Authorization", "Bearer AAAAp2gwDfo:APA91bEGspKYsM9M_XYQjcpBBBtcCs8YEnjXu-yJj4WGyEy3yDDANj-traeQQt2P4f_DTRvi6v6ciGHmC5Inpcggu-bm-dlxOaIs80ZTgx--RxaTAeJ8EEgyP9jgz2PQzx3hwoxKCYtQ")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }

    private void initUI() {
        tvUsernameToChat = findViewById(R.id.tvUsernameTochat);
        btnSendMsg = findViewById(R.id.btnSendMsg);
        btnBackChat = findViewById(R.id.btnBackChat);
        edtInputMsg = findViewById(R.id.edtTypeMessage);
        imgProfileUserOnChat = findViewById(R.id.picProfile);
        recyclerView = findViewById(R.id.rcvChatLine);

        tvUsernameToChat.setText(userToChat.getUsername().toString().trim());
        btnBackChat.setOnClickListener(view -> {
           onBackPressed();
        });
        getOrCreateChatRoom();
        setUpChatRCV();
    }

    private void setUpChatRCV() {
        Query query = FirebaseUtil.getChatRoomMessageRef(chatRoomID)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class).build();

        chatRecycleAdapter = new ChatRecycleAdapter(options,getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatRecycleAdapter);
        chatRecycleAdapter.startListening();
        chatRecycleAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
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