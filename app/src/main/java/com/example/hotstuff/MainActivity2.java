package com.example.hotstuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button Edit= findViewById(R.id.Edit);
        Edit.setOnClickListener(v->{
            Intent intent= new Intent(MainActivity2.this,MainActivity3.class);
            startActivity(intent);
        });
    }
}