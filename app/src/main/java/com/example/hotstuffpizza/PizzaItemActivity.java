package com.example.hotstuffpizza;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotstuffpizza.models.Pizza;
import com.example.hotstuffpizza.models.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class PizzaItemActivity extends AppCompatActivity {
    private ImageView pizzaImage;
    private TextView pizzaName;
    private TextView pizzaPrice;
    private TextView pizzaDescription;
    private EditText pizzaQty;
    private Button addToCartButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Pizza pizzaItem;

    private final String CART = "cart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_item);

        pizzaImage = findViewById(R.id.pizzaItemImage);
        pizzaName = findViewById(R.id.pizzaItemName);
        pizzaPrice = findViewById(R.id.pizzaItemPrice);
        pizzaDescription = findViewById(R.id.pizzaItemDescription);
        pizzaQty = findViewById(R.id.pizzaItemQty);
        addToCartButton = findViewById(R.id.pizzaItemAddToCart);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://hotstuffpizza-9734b-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference(CART);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String categoryId = intent.getStringExtra("categoryId");
        String name = intent.getStringExtra("name");
        double price = intent.getDoubleExtra("price", 0);
        String imageUrl = intent.getStringExtra("imageUrl");
        String description = intent.getStringExtra("description");

        pizzaItem = new Pizza();
        pizzaItem.setId(id);
        pizzaItem.setCategoryId(categoryId);
        pizzaItem.setName(name);
        pizzaItem.setPrice(price);
        pizzaItem.setImageUrl(imageUrl);
        pizzaItem.setDescription(description);

        if (!imageUrl.equals("")) {
            Picasso.get().load(pizzaItem.getImageUrl()).fit().centerCrop().into(pizzaImage);
        }
        pizzaName.setText(pizzaItem.getName());
        pizzaPrice.setText("Rs. "+ String.format("%.2f", pizzaItem.getPrice()) +"/=");
        pizzaDescription.setText(pizzaItem.getDescription());

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String qty = pizzaQty.getText().toString().trim();

            if (qty.equals("")) {
                Toast.makeText(PizzaItemActivity.this, "Please enter an amount.", Toast.LENGTH_SHORT).show();
            } else {
                Cart cart = new Cart();
                cart.setUserId(firebaseAuth.getCurrentUser().getUid());
                cart.setPizzaId(pizzaItem.getId());
                cart.setName(pizzaItem.getName());
                cart.setPerPrice(pizzaItem.getPrice());
                cart.setQuantity(Long.parseLong(qty));
                databaseReference.child(databaseReference.push().getKey()).setValue(cart);

                Toast.makeText(PizzaItemActivity.this, "Added to cart successfully.", Toast.LENGTH_SHORT).show();

                Intent intentBack = new Intent(PizzaItemActivity.this, MainActivity.class);
                startActivity(intentBack);
            }
            }
        });
    }
}