package com.example.hotstuffpizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    private List<Pizza> pizzaList = new ArrayList<>();
    private PizzaAdapter pizzaAdapter;
    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private final String PIZZA = "pizza";

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.homeRecyclerView);
        pizzaAdapter = new PizzaAdapter(pizzaList);

        firebaseDatabase = FirebaseDatabase.getInstance("https://hotstuffpizza-9734b-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference(PIZZA);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("pickAPizza", MODE_PRIVATE);
        final boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pizzaAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Pizza pizza = pizzaList.get(position);

                Intent intent;

                if (isAdmin) {
                    intent = new Intent(getActivity().getApplicationContext(), PizzaEditActivity.class);
                } else {
                    intent = new Intent(getActivity().getApplicationContext(), PizzaItemActivity.class);
                }
                intent.putExtra("id", pizza.getId());
                intent.putExtra("categoryId", pizza.getCategoryId());
                intent.putExtra("name", pizza.getName());
                intent.putExtra("price", pizza.getPrice());
                intent.putExtra("imageUrl", pizza.getImageUrl());
                intent.putExtra("description", pizza.getDescription());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        pizzaData();

        return view;
    }

    private void pizzaData() {
        databaseReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pizza pizzaItem;

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
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
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}