package com.example.cameraxdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> imageList;

        Button button = findViewById(R.id.button2);
        Button btnview = findViewById(R.id.view);
        Button btnNext = findViewById(R.id.btnNext);
        Button btnPre = findViewById(R.id.btnPre);
        ImageView img = findViewById(R.id.imageView);

        EditText inputURL = findViewById(R.id.inputURL);
        AtomicInteger index = new AtomicInteger();
        AtomicInteger index1 = new AtomicInteger();

        DatabaseHelper db = new DatabaseHelper(this);
        List<String> img1 = db.getUris();
        List<String> img2 = db.getUrls();
        int a = img1.size();
        int b = img2.size();

        btnNext.setOnClickListener(view -> {
            if(index.get() < a - 1){
                showImage(img, img1.get(index.get()));
                index.getAndIncrement();
            }else if(index.get() >= a - 1) {
               if(index1.get() <= b - 1){
                   index.getAndIncrement();
                   Picasso.get()
                           .load(img2.get(index1.get()).toString())
                           .into(img);

                   index1.getAndIncrement();
               }

            }

        });

        btnPre.setOnClickListener(view -> {
            if(index.get() <= a + b - 1){
                if(index1.get() > 0){
                    index1.getAndDecrement();
                    index.getAndDecrement();
                    Picasso.get()
                            .load(img2.get(index1.get()).toString())
                            .into(img);
                }
                if(index.get() <= a -1){
                    if(index.get() > 0){
                        showImage(img, img1.get(index.get()));
                        index.getAndDecrement();
                    }
                }
            }

        });
        btnview.setOnClickListener(view -> {
            ArrayAdapter<String> array =
                    new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, img1);
            ListView list = findViewById(R.id.listurl);

            list.setAdapter(array);
        });

        button.setOnClickListener(view -> {
            Intent intent = new Intent(this, TakePic.class);
            startActivity(intent);
        });


        inputURL.setText("https://upload.wikimedia.org/wikipedia/commons/1/1c/Ho_Chi_Minh_1946.jpg");

        Picasso.get()
                .load(inputURL.getText().toString())
                .into(img);


        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(view -> {
            String url = inputURL.getText().toString();
            db.insertUrl(url);
            Picasso.get()
                    .load(url)
                    .into(img);

        });



    }

    private void showImage(ImageView img, String img1){
        try {
            img.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse("file:///" + img1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}