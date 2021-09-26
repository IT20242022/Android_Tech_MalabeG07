package com.example.hotstuffpizza;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotstuffpizza.models.Pizza;
import com.example.hotstuffpizza.models.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PizzaEditActivity extends AppCompatActivity {
    private EditText imageUrlField;
    private EditText nameField;
    private EditText descriptionField;
    private EditText priceField;
    private Button editButton;
    private Spinner categorySpinner;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseCategoryReference;
    private Pizza pizzaForSave;
    private boolean isEdit = false;
    private boolean isAdd = false;
    private ArrayAdapter<Category> categoryArrayAdapter;
    private List<Category> categoryList = new ArrayList<>();

    private final String PIZZA = "pizza";
    private final String CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_edit);

        imageUrlField = findViewById(R.id.pizzaEditImageUrl);
        nameField = findViewById(R.id.pizzaEditName);
        descriptionField = findViewById(R.id.pizzaEditDescription);
        priceField = findViewById(R.id.pizzaEditPrice);
        editButton = findViewById(R.id.pizzaEditButton);
        categorySpinner = findViewById(R.id.pizzaEditCategorySpinner);

        firebaseDatabase = FirebaseDatabase.getInstance("https://hotstuffpizza-9734b-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference(PIZZA);
        databaseCategoryReference = firebaseDatabase.getReference(CATEGORY);

        categoryArrayAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categoryList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);
                ((TextView)view.findViewById(android.R.id.text1)).setText(categoryList.get(position).getName());
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getDropDownView(position, convertView, parent);
                ((TextView)view.findViewById(android.R.id.text1)).setText(categoryList.get(position).getName());
                return view;
            }
        };
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(categoryArrayAdapter);

        pizzaForSave = new Pizza();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        if (id.equals("")) {
            isAdd = true;
            editButton.setText("Add");
        } else {
            String categoryId = intent.getStringExtra("categoryId");
            String name = intent.getStringExtra("name");
            double price = intent.getDoubleExtra("price", 0);
            String imageUrl = intent.getStringExtra("imageUrl");
            String description = intent.getStringExtra("description");

            pizzaForSave.setId(id);
            pizzaForSave.setCategoryId(categoryId);
            pizzaForSave.setName(name);
            pizzaForSave.setPrice(price);
            pizzaForSave.setImageUrl(imageUrl);
            pizzaForSave.setDescription(description);

            updateUI();

            imageUrlField.setEnabled(false);
            nameField.setEnabled(false);
            descriptionField.setEnabled(false);
            priceField.setEnabled(false);
            categorySpinner.setEnabled(false);
        }

        loadCategories();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUrlField.getText().toString().equals("") || imageUrlField.getText() == null) {
                    Toast.makeText(PizzaEditActivity.this, "Please enter image URL.", Toast.LENGTH_SHORT).show();
                } else {
                    pizzaForSave.setImageUrl(imageUrlField.getText().toString());
                    pizzaForSave.setName(nameField.getText().toString());
                    pizzaForSave.setPrice(Double.parseDouble(priceField.getText().toString()));
                    pizzaForSave.setDescription(descriptionField.getText().toString());

                    if (((Category) categorySpinner.getSelectedItem()).getId() != null) {
                        pizzaForSave.setCategoryId(((Category) categorySpinner.getSelectedItem()).getId());
                    }

                    if (isAdd) {
                        databaseReference.child(databaseReference.push().getKey()).setValue(pizzaForSave);
                        Toast.makeText(PizzaEditActivity.this, "Pizza added successfully.", Toast.LENGTH_SHORT).show();

                        Intent backIntent = new Intent(PizzaEditActivity.this, AdminMainActivity.class);
                        startActivity(backIntent);
                    } else {
                        if (isEdit) {
                            databaseReference.child(pizzaForSave.getId()).setValue(pizzaForSave);
                            Toast.makeText(PizzaEditActivity.this, "Pizza updated successfully.", Toast.LENGTH_SHORT).show();
                            Intent backIntent = new Intent(PizzaEditActivity.this, AdminMainActivity.class);
                            startActivity(backIntent);
                            finish();
                        } else {
                            isEdit = true;

                            imageUrlField.setEnabled(true);
                            nameField.setEnabled(true);
                            priceField.setEnabled(true);
                            descriptionField.setEnabled(true);
                            categorySpinner.setEnabled(true);
                            editButton.setText("Save");
                        }
                    }
                }
            }
        });
    }

    private void loadCategories() {
        databaseCategoryReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Category categoryItem;

                categoryList.add(new Category("Select a Category", null));

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot item: dataSnapshot.getChildren()) {
                        categoryItem = item.getValue(Category.class);
                        categoryItem.setId(item.getKey());

                        if (categoryItem != null) {
                            categoryList.add(categoryItem);
                        }
                    }

                    categoryArrayAdapter.notifyDataSetChanged();

                    for (Category item: categoryList) {
                        if (pizzaForSave.getCategoryId() != null && item.getId() != null && item.getId().equals(pizzaForSave.getCategoryId())) {
                            categorySpinner.setSelection(categoryList.indexOf(item));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void updateUI() {
        imageUrlField.setText(pizzaForSave.getImageUrl());
        nameField.setText(pizzaForSave.getName());
        priceField.setText(Double.toString(pizzaForSave.getPrice()));
        descriptionField.setText(pizzaForSave.getDescription());
    }
}