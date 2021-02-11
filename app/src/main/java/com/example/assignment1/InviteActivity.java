package com.example.assignment1;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InviteActivity extends AppCompatActivity {

    private static final String TAG = "InviteActivity";

    private TextView displayDate;
    private Spinner spinner;
    private DatePickerDialog.OnDateSetListener dateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.guest_list_item);


        //get the spinner from the xml.
        spinner = findViewById(R.id.provinces_dropdown);
        //create a list of items for the spinner.
        //String[] items = new String[]{"Alberta", "British Columbia", "Manitoba",
        //      "New Brunswick", "Newfoundland and Labrador", "Nova Scotia", "Ontario",
        //    "Prince Edward Island", "Quebec", "Saskatchewan"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        // ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        // dropdown.setAdapter(adapter);

        List<String> provinces = new ArrayList<>();
        provinces.add(0, "Choose Province");
        provinces.add("Alberta");
        provinces.add("British Columbia");
        provinces.add("Manitoba");
        provinces.add("New Brunswick");
        provinces.add("Newfoundland and Labrador");
        provinces.add("Nova Scotia");
        provinces.add("Ontario");
        provinces.add("Prince Edward Island");
        provinces.add("Quebec");
        provinces.add("Saskatchewan");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose Province"))
                {
                    String item = parent.getItemAtPosition(position).toString();

                    //     Toast.makeText(parent.getContext(), "Selected: " +item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        displayDate = (TextView) findViewById(R.id.selectDate);

        displayDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        InviteActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateListener,
                        year, month, day
                );

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day){
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                displayDate.setText(date);
            }
        };



        TextView t1;
        t1= findViewById(R.id.whoToInvite);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("GuestPrefs", Context.MODE_PRIVATE);
        String curGuest = sp.getString("cur_guest","");
        //if cur_guest is null
        if (curGuest==null)
        {
            curGuest="SORRY IT IS NULL";
        }

        // Toast.makeText(InviteActivity.this, "HEY THS IS"+curGuest, Toast.LENGTH_SHORT).show();
        t1.setText("Invitation to: "+curGuest);
        // t1.setText("HELLO THERE");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        //DatePickerBuilder dpb = new DatePickerBuilder()
        //       .setFragmentManager(getSupportFragmentManager())
        //      .setStyleResId(R.style.BetterPickersDialogFragment)
        //     .setYearOptional(true);
        //dpb.show();
        //}
    }


    public void backToList(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void sendInvitation(View view)
    {
        Toast.makeText(InviteActivity.this, "Invitation sent", Toast.LENGTH_SHORT).show();
    }
}

