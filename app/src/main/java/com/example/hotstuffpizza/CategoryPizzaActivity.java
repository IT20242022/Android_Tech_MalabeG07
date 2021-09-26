package com.example.hotstuffpizza;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotstuffpizza.adapters.PizzaAdapter;
import com.example.hotstuffpizza.models.Pizza;
import com.example.hotstuffpizza.utils.RecyclerTouchListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryPizzaActivity extends AppCompatActivity {
    private List<Pizza> pizzaList = new ArrayList<>();
    private PizzaAdapter pizzaAdapter;
    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private final String PIZZA = "pizza";
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_pizza);

        recyclerView = findViewById(R.id.categoryPizzaRecyclerView);
        pizzaAdapter = new PizzaAdapter(pizzaList);

        firebaseDatabase = FirebaseDatabase.getInstance("https://hotstuffpizza-9734b-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference(PIZZA);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pizzaAdapter);

        categoryId = getIntent().getStringExtra("id");

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Pizza pizza = pizzaList.get(position);

                Intent intent = new Intent(getApplicationContext(), PizzaItemActivity.class);
                intent.putExtra("id", pizza.getId());
                intent.putExtra("categoryId", pizza.getCategoryId());
                intent.putExtra("name", pizza.getName());
                intent.putExtra("price", Double.toString(pizza.getPrice()));
                intent.putExtra("imageUrl", pizza.getImageUrl());
                intent.putExtra("description", pizza.getDescription());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) { }
        }));

        pizzaData();
    }

    private void pizzaData() {
        databaseReference.orderByChild("categoryId").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pizza pizzaItem;

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot item: dataSnapshot.getChildren()) {
                        pizzaItem = item.getValue(Pizza.class);
                        pizzaItem.setId(item.getKey());

                        if (pizzaItem != null) {
                            pizzaList.add(pizzaItem);
                        }
                    }
                    pizzaAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}