package com.example.hotstuffpizza;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hotstuffpizza.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText addressField;
    private Button editButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private User userForSave;
    private boolean isEdit = false;

    private final String USER = "user";

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firstNameField = view.findViewById(R.id.profileFirstName);
        lastNameField = view.findViewById(R.id.profileLastName);
        addressField = view.findViewById(R.id.profileAddress);
        editButton = view.findViewById(R.id.profileEditButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://hotstuffpizza-9734b-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference(USER);

        userForSave = new User();

        loadUserData();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit) {
                    userForSave.setFirstName(firstNameField.getText().toString());
                    userForSave.setLastName(lastNameField.getText().toString());
                    userForSave.setAddress(addressField.getText().toString());

                    databaseReference.child(userForSave.getId()).setValue(userForSave);

                    Toast.makeText(getActivity().getApplicationContext(), "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                    firstNameField.setEnabled(false);
                    lastNameField.setEnabled(false);
                    addressField.setEnabled(false);
                    editButton.setText("Edit");
                    isEdit = false;
                } else {
                    isEdit = true;

                    firstNameField.setEnabled(true);
                    lastNameField.setEnabled(true);
                    addressField.setEnabled(true);
                    editButton.setText("Save");
                }
            }
        });

        return view;
    }

    private void loadUserData() {
        databaseReference.orderByChild("email").equalTo(firebaseAuth.getCurrentUser().getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 1) {
                    for (DataSnapshot item: dataSnapshot.getChildren()) {
                        userForSave = item.getValue(User.class);
                        userForSave.setId(item.getKey());
                    }
                    updateUI();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void updateUI() {
        firstNameField.setText(userForSave.getFirstName());
        lastNameField.setText(userForSave.getLastName());
        addressField.setText(userForSave.getAddress());

        firstNameField.setEnabled(false);
        lastNameField.setEnabled(false);
        addressField.setEnabled(false);
    }
}