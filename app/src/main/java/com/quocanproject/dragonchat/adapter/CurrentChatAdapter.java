package com.quocanproject.dragonchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.quocanproject.dragonchat.ChatActivity;
import com.quocanproject.dragonchat.R;
import com.quocanproject.dragonchat.model.ChatRoom;
import com.quocanproject.dragonchat.model.User;
import com.quocanproject.dragonchat.utils.AndroidUtil;
import com.quocanproject.dragonchat.utils.FirebaseUtil;

public class CurrentChatAdapter extends FirestoreRecyclerAdapter<ChatRoom, CurrentChatAdapter.ChatRoomViewHolder> {

    Context context;

    public CurrentChatAdapter(@NonNull FirestoreRecyclerOptions<ChatRoom> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position, @NonNull ChatRoom model) {
        FirebaseUtil.getOtherUserFromChatRoom(model.getUserIDs())
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        boolean isLastMsgSendByMe = model.getLastMsgSenderID().equals(FirebaseUtil.currentUserID());

                        User otherUser = task.getResult().toObject(User.class);
                        holder.username.setText(otherUser.getUsername());

                        if(isLastMsgSendByMe){
                            holder.lastMsg.setText("You: "+model.getLastMsg());
                        }
                        else
                            holder.lastMsg.setText(model.getLastMsg());
                            holder.lastMsgTimestamp.setText(FirebaseUtil.TimestampToString(model.getLastMsgTimestamp()));

                            holder.itemView.setOnClickListener(view -> {
                                Intent intent = new Intent(context, ChatActivity.class);
                                AndroidUtil.passUserDataAsIntent(intent, otherUser);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });
                    }
                });



    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.chat_current_row_item, parent, false);

        return new ChatRoomViewHolder(view);
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder{

        TextView username, lastMsg, lastMsgTimestamp;
        ImageView profilePic;
        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.tvUsernameHome);
            lastMsg = itemView.findViewById(R.id.tvLastMessageHome);
            lastMsgTimestamp = itemView.findViewById(R.id.tvTimestampLastSendHome);
            profilePic = itemView.findViewById(R.id.picProfile);

        }
    }
}
