package com.example.individualwork;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import entity.Trip;

public class ListTrip extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_trip);

        Database dbHelper = new Database(getApplicationContext());
        List<Trip> trips = dbHelper.getTrip();

        ListView tv = findViewById(R.id.list);

        Button reset = findViewById(R.id.btnReset);
        Button search = findViewById(R.id.search);
        search.setOnClickListener(view -> {
            EditText text = findViewById(R.id.searchText);
            Database db = new Database(this);
            List<String> searchTr = db.searchTrip(text.getText().toString());
            ArrayAdapter<String> array1 =
                    new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, searchTr);
            tv.setAdapter(array1);
        });

        reset.setOnClickListener(view -> {
            Database db = new Database(this);
            db.ResetData();
            Toast.makeText(this, "Delete all data success ", Toast.LENGTH_SHORT).show();
        });

        ArrayAdapter<Trip> array =
                new ArrayAdapter<Trip>(getApplicationContext(), android.R.layout.simple_list_item_1, trips);
        tv.setAdapter(array);

        tv.setOnItemClickListener(((adapterView, view, i, l) -> {
            Trip select = array.getItem(i);
            final String[] options = {"Details","Edit","Delete","Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setItems(options, (dialog, item) ->{
                if(options[item] == "Details"){
                    openDetails(select);
                }else if (options[item] == "Delete"){
                    Database db = new Database(this);
                    db.DeleteTrip(select.getId());
                    Toast.makeText(this, "Delete success " + select.getName(), Toast.LENGTH_SHORT).show();
                }else if(options[item] == "Cancel"){
                    dialog.dismiss();
                }else if(options[item] == "Edit" ){
                    Edit(select);
                }
            });
            builder.show();
        }));

        BottomNavigationView nav = findViewById(R.id.Navigation);
        nav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.page1:
                    goToHome();
                    return true;
                case R.id.page2:
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

    private void openDetails(Trip select) {
        Intent intent = new Intent(ListTrip.this, Details.class);
        intent.putExtra("trip_id",select.getId());
        intent.putExtra("trip_name", select.getName());
        intent.putExtra("date", select.getDate());
        intent.putExtra("des", select.getDescription());
        intent.putExtra("destination", select.getDestination());
        intent.putExtra("pic", select.getPic());
        intent.putExtra("risk", select.isRisk());

        startActivity(intent);
    }

    private void Edit(Trip select) {
        Intent intent = new Intent(ListTrip.this, EditTrip.class);
        intent.putExtra("trip_id",select.getId());
        intent.putExtra("trip_name", select.getName());
        intent.putExtra("date", select.getDate());
        intent.putExtra("des", select.getDescription());
        intent.putExtra("destination", select.getDestination());
        intent.putExtra("pic", select.getPic());
        intent.putExtra("risk", select.isRisk());

        startActivity(intent);
    }
}