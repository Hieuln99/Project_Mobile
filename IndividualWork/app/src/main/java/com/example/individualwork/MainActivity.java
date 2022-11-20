package com.example.individualwork;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText name = findViewById(R.id.txtName);
        EditText date = findViewById(R.id.txtDate);
        EditText des = findViewById(R.id.txtDestination);
        EditText description = findViewById(R.id.txtDescription);
        EditText pic = findViewById(R.id.txtPicture);
        RadioGroup button = findViewById(R.id.Risk);
        RadioButton yes = findViewById(R.id.yesButton);
        RadioButton no = findViewById(R.id.noButton);

        yes.isChecked();


        AtomicBoolean risk = new AtomicBoolean(true);
        yes.setOnClickListener(view -> {
            risk.set(true);
        });
        no.setOnClickListener(view -> {
            risk.set(false);
        });

        date.setOnFocusChangeListener((view, b) -> {
            if(b){
                DatePickerFragment dlg = new DatePickerFragment();
                dlg.setDateInput(date);
                dlg.show(getSupportFragmentManager(), "datePicker");
            }
        });


        Button add = findViewById(R.id.button);
        add.setOnClickListener(view -> {
            Database db = new Database(getApplicationContext());

            if(validate(name.getText().toString(), date.getText().toString(), pic.getText().toString(),
                    des.getText().toString())){
                db.insertTrip(name.getText().toString(), date.getText().toString(),
                        description.getText().toString(),pic.getText().toString(),des.getText().toString(), risk.get());
                Toast.makeText(getApplicationContext(),"Add success trip ",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Add false trip ",Toast.LENGTH_LONG).show();
            }
        });

        BottomNavigationView nav = findViewById(R.id.Navigation);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page1:
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
        TextView error= findViewById(R.id.error);

        EditText name1 = findViewById(R.id.txtName);
        EditText date1 = findViewById(R.id.txtDate);
        EditText des1 = findViewById(R.id.txtDestination);
        EditText pic1 = findViewById(R.id.txtPicture);
        boolean pass = true;

        if(name.isEmpty()){
            name1.setError("Name can not be null");
            pass = false;
        }
        if(date.isEmpty()){
            date1.setError("Date can not be null");
            pass = false;
        }
        if(pic.isEmpty()){
            pic1.setError("Date can not be null");
            pass = false;
        }
        if(des.isEmpty()){
            des1.setError("Date can not be null");
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
        public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

            EditText dateInput;

            public EditText getDateInput() {
                return dateInput;
            }

            public void setDateInput(EditText dateInput) {
                this.dateInput = dateInput;
            }


            @NonNull
            @Override
            public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                return new DatePickerDialog(requireContext(), this, year, month, day);
            }

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateInput.setText(String.valueOf(month + "/" + year + "/" + dayOfMonth));
            }
        }

    }