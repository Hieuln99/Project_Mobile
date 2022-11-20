package com.example.individualwork;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddDetailTrip extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail_trip);

        Intent intent = getIntent();
        int TripId = intent.getIntExtra("trip_id",0);
        String TripName = intent.getStringExtra("trip_name");

        EditText Trip = findViewById(R.id.Trip_Id_Ex);
        Trip.setText("Trip Id: " + String.valueOf(TripId) + " -- " + TripName);

        Button add = findViewById(R.id.AddEx);
        add.setOnClickListener(view -> {
            Database db = new Database(this);
            EditText costName = findViewById(R.id.Cost_Name);
            EditText cost = findViewById(R.id.Trip_Cost);

            long detail = db.insertDetail(TripId, costName.getText().toString(), cost.getText().toString());
            Toast.makeText(getApplicationContext(),"Add success details "+ costName.getText().toString(),Toast.LENGTH_LONG).show();
        });

        Button viewAll = findViewById(R.id.ViewAllEx);
        viewAll.setOnClickListener(view -> {

            Intent ListDetails = new Intent(this,ListDetails.class);
            ListDetails.putExtra("trip_id",TripId);
            startActivity(ListDetails);
        });
    }

}