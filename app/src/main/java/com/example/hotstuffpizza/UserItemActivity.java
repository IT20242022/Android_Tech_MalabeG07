package com.example.hotstuffpizza;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotstuffpizza.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class UserItemActivity extends AppCompatActivity {
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText addressField;
    private Switch isAdminSwitch;
    private Button editButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private User userForSave;
    private boolean isEdit = false;
    private boolean isAdd = false;

    private final String USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_item);

        firstNameField = findViewById(R.id.userItemFirstName);
        lastNameField = findViewById(R.id.userItemLastName);
        emailField = findViewById(R.id.userItemEmail);
        passwordField = findViewById(R.id.userItemPasswordField);
        confirmPasswordField = findViewById(R.id.userItemConfirmPassword);
        addressField = findViewById(R.id.userItemAddress);
        isAdminSwitch = findViewById(R.id.userItemIsAdminSwitch);
        editButton = findViewById(R.id.userItemEditButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://hotstuffpizza-9734b-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference(USER);

        userForSave = new User();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        if (id.equals("")) {
            isAdd = true;
            editButton.setText("Add");
        } else {
            String firstName = intent.getStringExtra("firstName");
            String lastName = intent.getStringExtra("lastName");
            String email = intent.getStringExtra("email");
            String address = intent.getStringExtra("address");
            boolean isAdmin = intent.getBooleanExtra("isAdmin", false);

            userForSave.setId(id);
            userForSave.setFirstName(firstName);
            userForSave.setLastName(lastName);
            userForSave.setEmail(email);
            userForSave.setAddress(address);
            userForSave.setIsAdmin(isAdmin);

            updateUI();

            firstNameField.setEnabled(false);
            lastNameField.setEnabled(false);
            addressField.setEnabled(false);
            isAdminSwitch.setEnabled(false);
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAdd) {
                    String firstName = firstNameField.getText().toString().trim();
                    String lastName = lastNameField.getText().toString().trim();
                    String email = emailField.getText().toString().trim();
                    String password = passwordField.getText().toString().trim();
                    String confirmPassword = confirmPasswordField.getText().toString().trim();
                    String address = addressField.getText().toString().trim();

                    if (firstName == null || firstName.equals("")) {
                        Toast.makeText(UserItemActivity.this, "First Name is Required.", Toast.LENGTH_SHORT).show();
                    } else if (lastName == null || lastName.equals("")) {
                        Toast.makeText(UserItemActivity.this, "Last Name is Required.", Toast.LENGTH_SHORT).show();
                    } else if (email == null || email.equals("")) {
                        Toast.makeText(UserItemActivity.this, "Email is Required.", Toast.LENGTH_SHORT).show();
                    } else if (!validateEmail(email)) {
                        Toast.makeText(UserItemActivity.this, "Email is not valid.", Toast.LENGTH_SHORT).show();
                    } else if (password == null || password.equals("")) {
                        Toast.makeText(UserItemActivity.this, "Password is Required.", Toast.LENGTH_SHORT).show();
                    } else if (password.length() <= 8) {
                        Toast.makeText(UserItemActivity.this, "Password must be more than 8 characters.", Toast.LENGTH_SHORT).show();
                    } else if (!password.equals(confirmPassword)) {
                        Toast.makeText(UserItemActivity.this, "Password is not matching.", Toast.LENGTH_SHORT).show();
                    } else if (address == null || address.equals("")) {
                        Toast.makeText(UserItemActivity.this, "Address is Required.", Toast.LENGTH_SHORT).show();
                    } else {
                        editButton.setEnabled(false);

                        final User userObject = new User(firstName, lastName, email, address, isAdminSwitch.isChecked());

                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(UserItemActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    addUserToDatabase(userObject);
                                                } else {
                                                    editButton.setEnabled(true);
                                                    Toast.makeText(UserItemActivity.this, "User adding failed.", Toast.LENGTH_SHORT).show();
                                                    }
                                            }
                                        }
                                );
                    }
                } else {
                    if (isEdit) {
                        userForSave.setFirstName(firstNameField.getText().toString().trim());
                        userForSave.setLastName(lastNameField.getText().toString().trim());
                        userForSave.setAddress(addressField.getText().toString().trim());
                        userForSave.setIsAdmin(isAdminSwitch.isChecked());

                        databaseReference.child(userForSave.getId()).setValue(userForSave);

                        Toast.makeText(UserItemActivity.this, "User updated successfully.", Toast.LENGTH_SHORT).show();
                        Intent backIntent = new Intent(UserItemActivity.this, AdminMainActivity.class);
                        startActivity(backIntent);
                        finish();
                    } else {
                        isEdit = true;

                        firstNameField.setEnabled(true);
                        lastNameField.setEnabled(true);
                        addressField.setEnabled(true);
                        isAdminSwitch.setEnabled(true);
                        editButton.setText("Save");
                    }
                }
            }
        });
    }

    private void addUserToDatabase(User user) {
        String keyId = databaseReference.push().getKey();
        databaseReference.child(keyId).setValue(user);
        Toast.makeText(UserItemActivity.this, "User added successfully!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(UserItemActivity.this, AdminMainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateEmail(String emailStr) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(emailStr).matches();
    }

    private void updateUI() {
        firstNameField.setText(userForSave.getFirstName());
        lastNameField.setText(userForSave.getLastName());
        addressField.setText(userForSave.getAddress());
        isAdminSwitch.setChecked(userForSave.getIsAdmin());

        emailField.setVisibility(View.GONE);
        passwordField.setVisibility(View.GONE);
        confirmPasswordField.setVisibility(View.GONE);
    }
}