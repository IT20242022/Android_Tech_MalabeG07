package com.example.hotstuffpizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotstuffpizza.models.Order;
import com.example.hotstuffpizza.models.Pizza;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

public class PaymentConfirmationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseOrderReference;
    private DatabaseReference databaseCartOrderMapReference;
    private SharedPreferences sharedPreferences;

    private Button editPaymentDetails;
    private Button deletePaymentDetails;
    private Button confirmPaymentDetails;
    private EditText cardHoldersNameField;
    private EditText paymentMethodField;
    private EditText cardNumberField;
    private EditText expirationDateField;
    private EditText deliveryMethodField;
    private EditText securityNumberField;
    private TextView totalAmount;

    private final String ORDER = "order";
    private final String CART_ORDER_MAP = "cartOrderMap";

    private String userEmail;
    private String userName;

    private Order orderForSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Confirm Payment and Delivery Details");

        editPaymentDetails = findViewById(R.id.editPayment);
        deletePaymentDetails = findViewById(R.id.deletePayment);
        confirmPaymentDetails = findViewById(R.id.confirmPayment);

        totalAmount = findViewById(R.id.totalAmount);
        cardHoldersNameField = findViewById(R.id.cardHoldersName);
        paymentMethodField = findViewById(R.id.paymentMethod);
        cardNumberField = findViewById(R.id.cardNumber);
        expirationDateField = findViewById(R.id.expirationDate);
        deliveryMethodField = findViewById(R.id.deliveryMethod);
        securityNumberField = findViewById(R.id.securityNumber);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://hotstuffpizza-9734b-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseOrderReference = firebaseDatabase.getReference(ORDER);
        databaseCartOrderMapReference = firebaseDatabase.getReference(CART_ORDER_MAP);

        Bundle bundle = this.getIntent().getExtras();
        String orderMapId =  bundle.getString("orderMapId");
        String orderId =  bundle.getString("orderId");
        double orderTotal =  bundle.getDouble("orderTotal");
        String cardHoldersName =  bundle.getString("cardHoldersName");
        String paymentMethod =  bundle.getString("paymentMethod");
        String cardNumber =  bundle.getString("cardNumber");
        String expirationDate =  bundle.getString("expirationDate");
        String securityCode =  bundle.getString("securityCode");
        String deliveryMethod =  bundle.getString("deliveryMethod");

        cardHoldersNameField.setText(cardHoldersName);
        paymentMethodField.setText(paymentMethod);
        cardNumberField.setText(cardNumber);
        expirationDateField.setText(expirationDate);
        securityNumberField.setText(securityCode);
        deliveryMethodField.setText(deliveryMethod);

        totalAmount.setText("Rs " + String.format("%.2f", orderTotal) + "/=");

        sharedPreferences = getApplicationContext().getSharedPreferences("pickAPizza", MODE_PRIVATE);
        userEmail = firebaseAuth.getCurrentUser().getEmail();
        userName = sharedPreferences.getString("loggedUserName", "");

        cardHoldersNameField.setEnabled(false);
        paymentMethodField.setEnabled(false);
        cardNumberField.setEnabled(false);
        expirationDateField.setEnabled(false);
        securityNumberField.setEnabled(false);
        deliveryMethodField.setEnabled(false);

        editPaymentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editPaymentDetails.getText().toString().equals("Edit")) {
                    editPaymentDetails.setText("Save");
                    cardHoldersNameField.setEnabled(true);
                    cardNumberField.setEnabled(true);
                    expirationDateField.setEnabled(true);
                    securityNumberField.setEnabled(true);
                } else  if (editPaymentDetails.getText().toString().equals("Save")){
                    databaseOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot item: dataSnapshot.getChildren()) {
                                if (item.getKey().equals(orderId)) {
                                    Order order= dataSnapshot.getValue(Order.class);
                                    orderForSave = order;
                                    if (order != null) {
                                        order.setCardHoldersName(cardHoldersNameField.getText().toString());
                                        order.setPaymentMethod(paymentMethodField.getText().toString());
                                        order.setCardNumber(cardNumberField.getText().toString());
                                        order.setExpirationDate(expirationDateField.getText().toString());
                                        order.setDeliveryMethod(deliveryMethodField.getText().toString());
                                        order.setEmail(userEmail);
                                        order.setUserId(firebaseAuth.getCurrentUser().getUid());
                                        order.setName(userName);
                                        order.setTotal(orderTotal);
                                        order.setPaymentConfirm(false);

                                        databaseOrderReference.child(orderId).setValue(order);
                                        updateUI();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    editPaymentDetails.setText("Edit");
                    cardHoldersNameField.setEnabled(false);
                    paymentMethodField.setEnabled(false);
                    cardNumberField.setEnabled(false);
                    expirationDateField.setEnabled(false);
                    securityNumberField.setEnabled(false);
                    deliveryMethodField.setEnabled(false);
                }
            }
        });

        deletePaymentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot item: dataSnapshot.getChildren()) {
                            if (item.getKey().equals(orderId)) {
                                Order order= dataSnapshot.getValue(Order.class);
                                if (order != null) {
                                    databaseOrderReference.child(orderId).removeValue();
                                    databaseCartOrderMapReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot item: snapshot.getChildren()) {
                                                if (item.getKey().equals(orderMapId)) {
                                                    Order order= snapshot.getValue(Order.class);
                                                    if (order != null) {
                                                        databaseCartOrderMapReference.child(orderMapId).removeValue();
                                                        Intent intent = new Intent(PaymentConfirmationActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


            }
        });

        confirmPaymentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot item: dataSnapshot.getChildren()) {
                            if (item.getKey().equals(orderId)) {
                                Order order= dataSnapshot.getValue(Order.class);
                                if (order != null) {
                                    order.setCardHoldersName(orderForSave.getCardHoldersName());
                                    order.setPaymentMethod(orderForSave.getPaymentMethod());
                                    order.setCardNumber(orderForSave.getCardNumber());
                                    order.setExpirationDate(orderForSave.getExpirationDate());
                                    order.setDeliveryMethod(orderForSave.getDeliveryMethod());
                                    order.setEmail(userEmail);
                                    order.setUserId(firebaseAuth.getCurrentUser().getUid());
                                    order.setName(userName);
                                    order.setTotal(orderTotal);
                                    order.setPaymentConfirm(true);

                                    databaseOrderReference.child(orderId).setValue(order);

                                    Toast.makeText(PaymentConfirmationActivity.this, "Your order placed", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(PaymentConfirmationActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }

    private void updateUI() {
        cardHoldersNameField.setText(orderForSave.getCardHoldersName());
        paymentMethodField.setText(orderForSave.getPaymentMethod());
        cardNumberField.setText(orderForSave.getCardNumber());
        expirationDateField.setText(orderForSave.getExpirationDate());
        deliveryMethodField.setText(orderForSave.getDeliveryMethod());
    }
}
