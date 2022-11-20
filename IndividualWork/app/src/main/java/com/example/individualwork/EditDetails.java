package com.example.individualwork;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);


        Intent intent = getIntent();
        int trip_id = intent.getIntExtra("trip_id",0);
        int detail_id = intent.getIntExtra("detail_id",0);
        String detail_name = intent.getStringExtra("detail_name");
        String detail_cost = intent.getStringExtra("detail_cost");

        EditText cost = findViewById(R.id.DTrip_Cost);
        EditText name = findViewById(R.id.DCost_Name);
        EditText id = findViewById(R.id.DTrip_Id_Ex);


        cost.setText(detail_cost);
        name.setText(detail_name);
//        id.setText(trip_id);
    }
}