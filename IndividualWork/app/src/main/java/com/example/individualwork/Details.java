package com.example.individualwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import entity.Trip;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        int id = intent.getIntExtra("trip_id", 0);
        String name = intent.getStringExtra("trip_name");
        String date = intent.getStringExtra("date");
        String des = intent.getStringExtra("des");
        String destination = intent.getStringExtra("destination");
        boolean risk = intent.getBooleanExtra("risk", true);
        String pic = intent.getStringExtra("pic");

        TextView view = findViewById(R.id.txtName1);
        TextView view1 = findViewById(R.id.txtDate1);
        TextView view2 = findViewById(R.id.txtDes1);
        TextView view3 = findViewById(R.id.txtDestination1);
        TextView view4 = findViewById(R.id.risk);

        view.setText("Name: "+ name);
        view1.setText("Date: "+ date);
        view2.setText("Description: "+  des);
        view3.setText("Destination: "+ destination);
        view4.setText("Risk " + (risk ? "Yes" : "No"));

        Database dbHelper = new Database(getApplicationContext());
        List<Trip> trips = dbHelper.getTrip();
        ArrayAdapter<Trip> array =
                new ArrayAdapter<Trip>(getApplicationContext(), android.R.layout.simple_list_item_1, trips);

        BottomNavigationView nav = findViewById(R.id.Navigation);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page1:
                        goToHome();
                        return true;
                    case R.id.page2:
                        goToPage1();
                        return true;
                }
                return  false;
            }
        });

        Button AddEx = findViewById(R.id.addExpenses);
        AddEx.setOnClickListener(view5 -> {
            Trip trip = array.getItem(id-1);
            openDetails(trip);
        });
    }
    private void openDetails(Trip trip) {
        Intent intent = new Intent(this,AddDetailTrip.class);
        intent.putExtra("trip_id",trip.getId());
        intent.putExtra("trip_name",trip.getName());
        startActivity(intent);
    }
    private void goToPage1() {
        Intent intent = new Intent(getApplicationContext(),ListTrip.class);
        startActivity(intent);
    }
    private void goToHome() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}