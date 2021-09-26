package com.example.hotstuffpizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotstuffpizza.adapters.CartOrderMapAdapter;
import com.example.hotstuffpizza.models.CartOrderMap;
import com.example.hotstuffpizza.models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderItemActivity extends AppCompatActivity {
    private TextView orderIdTextView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView priceTextView;
    private TextView orderItemDeliveryMethodTextView;
    private TextView orderItemPaymentMethodTextView;
    private TextView orderItemPaymentStatusTextView;
    private RecyclerView recyclerView;
    private Button placeOrderButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseOrderReference;
    private DatabaseReference databaseCartOrderMapReference;
    private SharedPreferences sharedPreferences;
    private List<CartOrderMap> cartOrderMapList = new ArrayList<>();
    private CartOrderMapAdapter cartOrderMapAdapter;
    private Order orderForSave;

    private final String ORDER = "order";
    private final String CART_ORDER_MAP = "cartOrderMap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item);

        orderIdTextView = findViewById(R.id.orderItemOrderId);
        nameTextView = findViewById(R.id.orderItemName);
        emailTextView = findViewById(R.id.orderItemEmail);
        priceTextView = findViewById(R.id.orderItemPrice);
        orderItemDeliveryMethodTextView = findViewById(R.id.orderItemDeliveryMethod);
        orderItemPaymentMethodTextView = findViewById(R.id.orderItemPaymentMethod);
        orderItemPaymentStatusTextView = findViewById(R.id.orderItemPaymentStatus);
        recyclerView = findViewById(R.id.orderItemRecyclerView);
        placeOrderButton = findViewById(R.id.orderItemPlaceOrderButton);
        cartOrderMapAdapter = new CartOrderMapAdapter(cartOrderMapList);

        sharedPreferences = getApplicationContext().getSharedPreferences("pickAPizza", MODE_PRIVATE);
        firebaseDatabase = FirebaseDatabase.getInstance("https://hotstuffpizza-9734b-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseOrderReference = firebaseDatabase.getReference(ORDER);
        databaseCartOrderMapReference = firebaseDatabase.getReference(CART_ORDER_MAP);

        if (!sharedPreferences.getBoolean("isAdmin", false)) {
            placeOrderButton.setVisibility(View.GONE);
        }

        orderForSave = new Order();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String userId = intent.getStringExtra("userId");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        double total = intent.getDoubleExtra("total", 0);
        boolean isComplete = intent.getBooleanExtra("isComplete", false);
        String deliveryMethod = intent.getStringExtra("deliveryMethod");
        String paymentMethod = intent.getStringExtra("paymentMethod");
        boolean paymentStatus = intent.getBooleanExtra("paymentStatus", true);

        orderForSave.setId(id);
        orderForSave.setUserId(userId);
        orderForSave.setName(name);
        orderForSave.setEmail(email);
        orderForSave.setTotal(total);
        orderForSave.setIsComplete(isComplete);
        orderForSave.setDeliveryMethod(deliveryMethod);
        orderForSave.setPaymentMethod(paymentMethod);
        orderForSave.setPaymentConfirm(paymentStatus);

        updateUI();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartOrderMapAdapter);

        cartData();

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderForSave.setIsComplete(true);

                databaseOrderReference.child(orderForSave.getId()).setValue(orderForSave);

                Toast.makeText(OrderItemActivity.this, "Order Placed successfully.", Toast.LENGTH_SHORT).show();
                Intent backIntent = new Intent(OrderItemActivity.this, AdminMainActivity.class);
                startActivity(backIntent);
                finish();
            }
        });
    }

    private void cartData() {
        databaseCartOrderMapReference.orderByChild("orderId").equalTo(orderForSave.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CartOrderMap cartItem;

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot cartObj : dataSnapshot.getChildren()) {
                        cartItem = cartObj.getValue(CartOrderMap.class);
                        cartItem.setId(cartObj.getKey());

                        if (cartItem != null) {
                            cartOrderMapList.add(cartItem);
                        }
                    }
                    cartOrderMapAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void updateUI() {
        orderIdTextView.setText("Order Id: " + orderForSave.getId());
        nameTextView.setText(orderForSave.getName());
        emailTextView.setText(orderForSave.getEmail());
        priceTextView.setText("Total Price: Rs. " + String.format("%.2f", orderForSave.getTotal()) + "/=");
        orderItemDeliveryMethodTextView.setText(orderForSave.getDeliveryMethod());
        orderItemPaymentMethodTextView.setText(orderForSave.getPaymentMethod());
        orderItemPaymentStatusTextView.setText(orderForSave.isPaymentConfirm() ? "Paid" : "Unpaid");

    }
}