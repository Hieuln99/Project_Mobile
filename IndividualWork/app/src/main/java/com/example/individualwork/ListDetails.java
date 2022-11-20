package com.example.individualwork;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import entity.Detail;

public class ListDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);

        Intent intent = getIntent();
        int id = intent.getIntExtra("trip_id", 0);
        Database dbHelper = new Database(getApplicationContext());
        List<Detail> details = dbHelper.getDetail(id);

        ArrayAdapter<Detail> array =
                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, details);

        ListView lisDetail = findViewById(R.id.listDetails);
        lisDetail.setAdapter(array);

        lisDetail.setOnItemClickListener((adapterView, view, i, l) -> {

            final String[] options = {"Edit","Delete","Cancel"};
            Detail selected = array.getItem(i);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setItems(options, ((dialog, item) -> {
                if(options[item] == "Delete" ){
                    Database db = new Database(this);
                    db.DeleteDetail(selected.getCost_Id());
                    Intent load = new Intent(getApplicationContext(),ListDetails.class);
                    startActivity(load);
                    Toast.makeText(getApplicationContext(),"Delete success "+ selected.getCost_name().toString(),Toast.LENGTH_LONG).show();
                }else if(options[item] == "Edit" ){
                    Edit(selected);
                }
                else{

                }
            }));
            builder.show();
        });

        BottomNavigationView nav = findViewById(R.id.Navigation);
        nav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.page1:
                    goToHome();
                    return true;
                case R.id.page2:
                    goToPage1();
                    return true;
            }
            return  false;
        });
    }

    private void goToPage1() {
        Intent intent = new Intent(getApplicationContext(),ListTrip.class);
        startActivity(intent);
    }
    private void goToHome() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    private void Edit(Detail select) {
        Intent intent = new Intent(ListDetails.this, EditDetails.class);
        intent.putExtra("detail_id", select.getCost_Id());
        intent.putExtra("detail_name", select.getCost_name());
        intent.putExtra("detail_cost", select.getCost());
        intent.putExtra("trip_id", select.getTrip_Id());

        startActivity(intent);
    }
}