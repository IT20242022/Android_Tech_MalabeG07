package com.example.hotstuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RVActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
RecyclerView recyclerView;
RVAdapter adapter;
DAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvactivity);
        swipeRefreshLayout= findViewById(R.id.swip);
        recyclerView=findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager =new LinearLayoutManager( this);
        recyclerView.setLayoutManager(manager);
        adapter=new RVAdapter(this);
        recyclerView.setAdapter(adapter);
        dao = new DAO();
        loadData();
    }

    private void loadData() {
        dao.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<add> emps = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    add emp =data.getValue(add.class);
                    emps.add(emp);
                }
                adapter.setItems(emps);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}