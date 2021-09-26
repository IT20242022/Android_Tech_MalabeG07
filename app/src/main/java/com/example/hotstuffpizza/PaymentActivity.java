package com.example.hotstuffpizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.hotstuffpizza.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity {

    private Button savePaymentDetails;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseOrderReference;
    private SharedPreferences sharedPreferences;

    private EditText cardHoldersNameField;
    private EditText paymentMethodField;
    private EditText cardNumberField;
    private EditText expirationDateField;
    private EditText deliveryMethodField;
    private EditText securityCodeField;
    private TextView totalAmount;

    private final String ORDER = "order";

    private String userEmail;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Bundle bundle = this.getIntent().getExtras();
        String orderMapId =  bundle.getString("orderMapId");
        String orderId =  bundle.getString("orderId");
        double orderTotal =  bundle.getDouble("orderTotal");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Enter Payment and Delivery Details");

        totalAmount = findViewById(R.id.totalAmountPayment);
        savePaymentDetails = findViewById(R.id.savePaymentDetails);
        cardHoldersNameField = findViewById(R.id.cardHoldersName);
        paymentMethodField = findViewById(R.id.paymentMethod);
        cardNumberField = findViewById(R.id.cardNumber);
        expirationDateField = findViewById(R.id.expirationDate);
        deliveryMethodField = findViewById(R.id.deliveryMethod);
        securityCodeField = findViewById(R.id.securityNumber);

        String totalAmountTxt = "Rs " + String.format("%.2f", orderTotal) + "/=";
        totalAmount.setText(totalAmountTxt);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://hotstuffpizza-9734b-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseOrderReference = firebaseDatabase.getReference(ORDER);

        sharedPreferences = getApplicationContext().getSharedPreferences("pickAPizza", MODE_PRIVATE);
        userEmail = firebaseAuth.getCurrentUser().getEmail();
        userName = sharedPreferences.getString("loggedUserName", "");

        savePaymentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the order details or remove and remove order map details
                databaseOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot item: dataSnapshot.getChildren()) {
                            if (item.getKey().equals(orderId)) {
                                Order order= dataSnapshot.getValue(Order.class);
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

                                    Toast.makeText(PaymentActivity.this, "Your order placed continue for the confirm payment.", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(PaymentActivity.this, PaymentConfirmationActivity.class);
                                    intent.putExtra("orderId", orderId);
                                    intent.putExtra("orderTotal", orderTotal);
                                    intent.putExtra("orderMapId", orderMapId);
                                    intent.putExtra("cardHoldersName", cardHoldersNameField.getText().toString());
                                    intent.putExtra("paymentMethod", paymentMethodField.getText().toString());
                                    intent.putExtra("cardNumber", cardNumberField.getText().toString());
                                    intent.putExtra("expirationDate", expirationDateField.getText().toString());
                                    intent.putExtra("deliveryMethod", deliveryMethodField.getText().toString());
                                    intent.putExtra("securityCode", securityCodeField.getText().toString());
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
}
