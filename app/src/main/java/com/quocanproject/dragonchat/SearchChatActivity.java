package com.quocanproject.dragonchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.quocanproject.dragonchat.adapter.SearchUserRCVAdapter;
import com.quocanproject.dragonchat.model.User;
import com.quocanproject.dragonchat.utils.AndroidUtil;
import com.quocanproject.dragonchat.utils.FirebaseUtil;

public class SearchChatActivity extends AppCompatActivity {

    ImageButton btnBack, btnSearch;
    RecyclerView recyclerView;
    EditText edtSearchInput;

    SearchUserRCVAdapter rcvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_chat);
        initUI();

        addAction();
    }

    private void addAction() {
        btnBack.setOnClickListener(view -> {
            //back to previous activity
            onClickBack();
        });


        btnSearch.setOnClickListener(view -> {
            //search
            onClickSearch();
        });
    }

    private void onClickSearch() {
        String textEntered = edtSearchInput.getText().toString().trim();
        if(textEntered.isEmpty()){
            edtSearchInput.setError("Invalid username!");
        }

        getSearchResult(textEntered);
    }

    private void getSearchResult(String resultEntered) {
        //AndroidUtil.showToast(getApplicationContext(), resultEntered);

        Query query = FirebaseUtil.allUserCollectionRef()
                .whereEqualTo("username", resultEntered);

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class).build();

        rcvAdapter = new SearchUserRCVAdapter(options,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(rcvAdapter);
        rcvAdapter.startListening();
    }

    private void onClickBack() {
        onBackPressed();
    }

    private void initUI() {
        btnSearch = findViewById(R.id.btnSearchChat);
        btnBack = findViewById(R.id.btnBack);
        edtSearchInput = findViewById(R.id.edtSearchChat);
        recyclerView = findViewById(R.id.rcvSearchResult);
        edtSearchInput.requestFocus();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (rcvAdapter!=null){
            rcvAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (rcvAdapter!=null){
            rcvAdapter.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rcvAdapter!=null){
            rcvAdapter.startListening();
        }
    }
}