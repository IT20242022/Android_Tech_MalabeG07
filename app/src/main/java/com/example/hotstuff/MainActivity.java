package com.example.hotstuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int  CAMERA_REQUEST=1888;
    ImageView imageView;
    Button Takephoto;

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST){
            Bitmap photo=(Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageView=(ImageView) this.findViewById(R.id.imageView) ;
        Button Takephoto =(Button) this.findViewById(R.id.Takephoto);

        Takephoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
            }

        });


        final EditText price = findViewById(R.id.price);
        final EditText description = findViewById(R.id.description);
        final EditText subcategory = findViewById(R.id.subcategory);
        final EditText maincategory = findViewById(R.id.maincategory);

        Button add = findViewById(R.id.add);
        Button open1= findViewById(R.id.open1);
        open1.setOnClickListener(v->{
            Intent intent= new Intent(MainActivity.this,RVActivity.class);
            startActivity(intent);
        });
        Button update4 = findViewById(R.id.update4);

        update4.setOnClickListener(v->{
            Intent intent= new Intent(MainActivity.this,UpdateActivity.class);
            startActivity(intent);
        });
        Button category= findViewById(R.id.category);
        category.setOnClickListener(v->{
            Intent intent= new Intent(MainActivity.this,MainActivity2.class);
            startActivity(intent);
        });
        DAO dao = new DAO();
        add.setOnClickListener(v -> {
            add emp = new add(price.getText().toString(), description.getText().toString(), subcategory.getText().toString(), maincategory.getText().toString());

            dao.add(emp).addOnSuccessListener(suc -> {
                Toast.makeText(this, "Record is Inserted", Toast.LENGTH_SHORT).show();
                    }
            );
        });



    }
}