package com.example.hotstuffpizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotstuffpizza.adapters.OrderAdapter;
import com.example.hotstuffpizza.models.Order;
import com.example.hotstuffpizza.utils.RecyclerTouchListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class OrderFragment extends Fragment {
    private List<Order> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private RecyclerView recyclerView;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

    private final String ORDER = "order";

    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        recyclerView = view.findViewById(R.id.orderRecyclerView);
        orderAdapter = new OrderAdapter(orderList);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("pickAPizza", MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://hotstuffpizza-9734b-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference(ORDER);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Order order = orderList.get(position);

                if (!order.getIsComplete() && order.isPaymentConfirm()) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), OrderItemActivity.class);
                    intent.putExtra("id", order.getId());
                    intent.putExtra("userId", order.getUserId());
                    intent.putExtra("name", order.getName());
                    intent.putExtra("email", order.getEmail());
                    intent.putExtra("total", order.getTotal());
                    intent.putExtra("isComplete", order.getIsComplete());
                    intent.putExtra("deliveryMethod", order.getDeliveryMethod());
                    intent.putExtra("paymentMethod", order.getPaymentMethod());
                    intent.putExtra("paymentStatus", order.isPaymentConfirm());
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        orderData();

        return view;
    }

    private void orderData() {
        boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);

        if (isAdmin) {
            databaseReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Order orderItem;

                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            orderItem = item.getValue(Order.class);
                            orderItem.setId(item.getKey());

                            if (orderItem != null && orderItem.isPaymentConfirm()) {
                                orderList.add(orderItem);
                            }
                        }
                        orderAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            databaseReference.orderByChild("userId").equalTo(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Order orderItem;

                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            orderItem = item.getValue(Order.class);
                            orderItem.setId(item.getKey());

                            if (orderItem != null) {
                                orderList.add(orderItem);
                            }
                        }
                        orderAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.setGroupVisible(R.menu.action_menu, false);
    }
}