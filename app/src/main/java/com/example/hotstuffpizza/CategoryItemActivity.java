package com.example.hotstuffpizza;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hotstuffpizza.models.Category;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CategoryItemActivity extends AppCompatActivity {
    private ImageView categoryImage;
    private EditText categoryName;
    private EditText categoryImageUrl;
    private Button editButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Category categoryForSave;
    private boolean isEdit = false;
    private boolean isAdd = false;

    private final String CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_item);

        categoryImage = findViewById(R.id.categoryItemImage);
        categoryName = findViewById(R.id.categoryItemName);
        categoryImageUrl = findViewById(R.id.categoryItemImageUrl);
        editButton = findViewById(R.id.categoryItemEditButton);

        firebaseDatabase = FirebaseDatabase.getInstance("https://hotstuffpizza-9734b-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference(CATEGORY);

        categoryForSave = new Category();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        if (id.equals("")) {
            isAdd = true;
            editButton.setText("Add");
        } else {
            String name = intent.getStringExtra("name");
            String imageUrl = intent.getStringExtra("imageUrl");

            categoryForSave.setId(id);
            categoryForSave.setName(name);
            categoryForSave.setImageUrl(imageUrl);

            updateUI();

            categoryImageUrl.setEnabled(false);
            categoryName.setEnabled(false);
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdd) {
                    categoryForSave.setImageUrl(categoryImageUrl.getText().toString());
                    categoryForSave.setName(categoryName.getText().toString());

                    databaseReference.child(databaseReference.push().getKey()).setValue(categoryForSave);

                    Toast.makeText(CategoryItemActivity.this, "Category added successfully.", Toast.LENGTH_SHORT).show();

                    Intent backIntent = new Intent(CategoryItemActivity.this, AdminMainActivity.class);
                    startActivity(backIntent);
                } else {
                    if (isEdit) {
                        categoryForSave.setImageUrl(categoryImageUrl.getText().toString());
                        categoryForSave.setName(categoryName.getText().toString());

                        databaseReference.child(categoryForSave.getId()).setValue(categoryForSave);

                        Toast.makeText(CategoryItemActivity.this, "Category updated successfully.", Toast.LENGTH_SHORT).show();

                        Intent backIntent = new Intent(CategoryItemActivity.this, AdminMainActivity.class);
                        startActivity(backIntent);
                        finish();
                    } else {
                        isEdit = true;

                        categoryImageUrl.setEnabled(true);
                        categoryName.setEnabled(true);
                        editButton.setText("Save");
                    }
                }
            }
        });
    }

    private void updateUI() {
        if (!categoryForSave.getImageUrl().equals("")) {
            Picasso.get().load(categoryForSave.getImageUrl()).fit().centerCrop().into(categoryImage);
        }
        categoryImageUrl.setText(categoryForSave.getImageUrl());
        categoryName.setText(categoryForSave.getName());
    }
}