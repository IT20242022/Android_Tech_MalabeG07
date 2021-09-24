package com.example.hotstuff;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        final EditText price = findViewById(R.id.price);
        final EditText description = findViewById(R.id.description);
        final EditText subcategory = findViewById(R.id.subcategory);
        final EditText maincategory = findViewById(R.id.maincategory);

        Button update4 = findViewById(R.id.update4);
        Button delete4 = findViewById(R.id.delete4);
        DAO dao = new DAO();
        update4.setOnClickListener(v->{
            HashMap<String,Object> hashMap =new HashMap<>();
            hashMap.put("price",price.getText().toString());
            hashMap.put("description",description.getText().toString());
            hashMap.put("subcategory",subcategory.getText().toString());
            hashMap.put("maincategory",maincategory.getText().toString());
            dao.update("hotstuf-d0511-default-rtdb" ,hashMap).addOnSuccessListener(suc -> {
                        Toast.makeText(this, "Record is Update", Toast.LENGTH_SHORT).show();
                    }
            );
        });
        delete4.setOnClickListener(v->{
            HashMap<String,Object> hashMap =new HashMap<>();
            hashMap.put("price",price.getText().toString());
            hashMap.put("description",description.getText().toString());
            hashMap.put("subcategory",subcategory.getText().toString());
            hashMap.put("maincategory",maincategory.getText().toString());
            dao.remove("hotstuf-d0511-default-rtdb").addOnSuccessListener(suc -> {
                        Toast.makeText(this, "Record is removed", Toast.LENGTH_SHORT).show();
                    }
            );
        });

    }
}