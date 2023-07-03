package com.quocanproject.dragonchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.quocanproject.dragonchat.ChatActivity;
import com.quocanproject.dragonchat.R;
import com.quocanproject.dragonchat.model.ChatMessage;
import com.quocanproject.dragonchat.utils.AndroidUtil;
import com.quocanproject.dragonchat.utils.FirebaseUtil;

public class ChatRecycleAdapter extends FirestoreRecyclerAdapter<ChatMessage, ChatRecycleAdapter.ChatViewHolder> {

    Context context;

    public ChatRecycleAdapter(@NonNull FirestoreRecyclerOptions<ChatMessage> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull ChatMessage model) {

        if (model.getSenderID().equals(FirebaseUtil.currentUserID())){
            holder.chatSendLayout.setVisibility(View.VISIBLE);
            holder.chatReceiveLayout.setVisibility(View.GONE);
            holder.tvSender.setText(model.getMsg());
        } else {
            holder.chatSendLayout.setVisibility(View.GONE);
            holder.chatReceiveLayout.setVisibility(View.VISIBLE);
            holder.tvReceiver.setText(model.getMsg());
        }

    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.chat_msg_row, parent, false);

        return new ChatViewHolder(view);
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{

        LinearLayout chatSendLayout, chatReceiveLayout;
        TextView tvSender, tvReceiver;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatSendLayout = itemView.findViewById(R.id.layoutSend);
            chatReceiveLayout = itemView.findViewById(R.id.layoutReceive);
            tvSender = itemView.findViewById(R.id.tvSend);
            tvReceiver = itemView.findViewById(R.id.tvReceive);
        }
    }
}
