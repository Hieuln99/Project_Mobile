package com.example.individualwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.concurrent.atomic.AtomicBoolean;

public class EditTrip extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        Intent intent = getIntent();
        int id = intent.getIntExtra("trip_id", 0);
        String name1 = intent.getStringExtra("trip_name");
        String date1 = intent.getStringExtra("date");
        String des1 = intent.getStringExtra("des");
        String destination1 = intent.getStringExtra("destination");
        boolean risk1 = intent.getBooleanExtra("risk", true);
        String pic1 = intent.getStringExtra("pic");

        EditText name = findViewById(R.id.txtEName);
        EditText date = findViewById(R.id.txtEDate);
        EditText des = findViewById(R.id.txtEDestination);
        EditText description = findViewById(R.id.txtEDescription);
        EditText pic = findViewById(R.id.txtEPicture);
        RadioGroup button = findViewById(R.id.ERisk);
        RadioButton yes = findViewById(R.id.yesEButton);
        RadioButton no = findViewById(R.id.noEButton);

            name.setText(name1);
            date.setText(date1);
            des.setText(destination1);
            description.setText(des1);
            pic.setText(pic1);
            if(risk1 == true){
                yes.setChecked(true);
            }else {
                no.setChecked(true);
            }


        AtomicBoolean risk = new AtomicBoolean(true);
        yes.setOnClickListener(view -> {
            risk.set(true);
        });
        no.setOnClickListener(view -> {
            risk.set(false);
        });

        Button add = findViewById(R.id.Ebutton);
        add.setOnClickListener(view -> {
            Database db = new Database(getApplicationContext());

            if(validate(name.getText().toString(), date.getText().toString(), pic.getText().toString(),
                    des.getText().toString())){
                db.EditTrip(id ,name.getText().toString(), date.getText().toString(),
                        description.getText().toString(),pic.getText().toString(), des.getText().toString(), risk.get());
                goToPage1();
                Toast.makeText(getApplicationContext(),"Edit success trip ",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Edit false trip ",Toast.LENGTH_LONG).show();
            }
        });

        BottomNavigationView nav = findViewById(R.id.Navigation);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page1:
                        goToHome();
                        return false;
                    case R.id.page2:
                        goToPage1();
                        return true;
                }
                return false;
            }
        });

    }

    private boolean validate(String name, String date, String pic, String des) {
        String text = "";
        TextView error= findViewById(R.id.Eerror);

        boolean pass = true;

        if(name.isEmpty()){
            text += "Name can not be null\n";
            pass = false;
        }
        if(date.isEmpty()){
            text += "Date can not be null\n";
            pass = false;
        }
        if(pic.isEmpty()){
            text += "Picture can not be null\n";
            pass = false;
        }
        if(des.isEmpty()){
            text += "Destination can not be null\n";
            pass = false;
        }
        error.setText(text);
        return pass;
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