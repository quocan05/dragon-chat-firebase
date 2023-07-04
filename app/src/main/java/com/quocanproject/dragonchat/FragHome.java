package com.quocanproject.dragonchat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.quocanproject.dragonchat.adapter.CurrentChatAdapter;
import com.quocanproject.dragonchat.adapter.SearchUserRCVAdapter;
import com.quocanproject.dragonchat.model.ChatRoom;
import com.quocanproject.dragonchat.model.User;
import com.quocanproject.dragonchat.utils.FirebaseUtil;


public class FragHome extends Fragment {


    RecyclerView rcvHomeChatCurrent;
    CurrentChatAdapter currentChatAdapter;


    public FragHome() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_frag_home, container, false);
        rcvHomeChatCurrent = view.findViewById(R.id.rcvHomeChatCurrent);
        setupRecycleview();
        return view;
    }

    private void setupRecycleview() {
        //AndroidUtil.showToast(getApplicationContext(), resultEntered);

        Query query = FirebaseUtil.allChatRoomCollectionRef()
                .whereArrayContains("userIDs", FirebaseUtil.currentUserID())
                .orderBy("lastMsgTimestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatRoom> options = new FirestoreRecyclerOptions.Builder<ChatRoom>()
                .setQuery(query, ChatRoom.class).build();

        currentChatAdapter = new CurrentChatAdapter(options,getContext());
        rcvHomeChatCurrent.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvHomeChatCurrent.setAdapter(currentChatAdapter);
        currentChatAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (currentChatAdapter!=null){
            currentChatAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (currentChatAdapter!=null){
            currentChatAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentChatAdapter!=null){
            currentChatAdapter.startListening();
        }
    }
}