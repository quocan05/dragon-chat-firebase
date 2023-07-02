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
import com.quocanproject.dragonchat.ChatActivity;
import com.quocanproject.dragonchat.R;
import com.quocanproject.dragonchat.model.User;
import com.quocanproject.dragonchat.utils.AndroidUtil;
import com.quocanproject.dragonchat.utils.FirebaseUtil;

public class SearchUserRCVAdapter extends FirestoreRecyclerAdapter<User, SearchUserRCVAdapter.UserViewHolder> {

    Context context;

    public SearchUserRCVAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {

        holder.tvUsername.setText(model.getUsername());
        holder.tvPhoneNumber.setText(model.getPhone());
        if (model.getuID().equals(FirebaseUtil.currentUserID())){
            holder.tvUsername.setText(model.getUsername() +"(YOU)");
        }

        holder.itemView.setOnClickListener(view -> {
            //navigate to chat box
            Intent intent = new Intent(context, ChatActivity.class);
            AndroidUtil.passUserDataAsIntent(intent, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rcv_item_search, parent, false);

        return new UserViewHolder(view);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        TextView tvUsername;
        TextView tvPhoneNumber;
        ImageView imgProfile;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            imgProfile = itemView.findViewById(R.id.picProfile);
        }
    }
}
