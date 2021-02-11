package com.example.assignment1;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateEventActivity extends MainActivity{

    public static final String TAG = "CreateEventActivity";
    private Button addGuests = null;
    private int mYear =0, mMonth=0, mDay=0;
    private Spinner eventTypeSpinner = null;
    private TextView eventName=null;
    private TextView date = null;
    private TextView address = null;
    private TextView guests = null;
    private TextView menu = null;
    private TextView supplies = null;
    private Calendar cal = null;
    private final DatePickerDialog.OnDateSetListener callbackMethod = null;
    private SharedPreferences savedValues = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        savedValues = getSharedPreferences("Saved Values", MODE_PRIVATE);

        eventName = findViewById(R.id.txtEventName);
        address = findViewById(R.id.editAddress);
        guests = findViewById(R.id.listOfGuests);
        menu = findViewById(R.id.txtMenu);
        supplies = findViewById(R.id.txtSupplies);

        //go back to main page
        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        addGuests = findViewById(R.id.btnAddGuests);
        addGuests.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent guestIntent = new Intent(CreateEventActivity.this, GuestActivity.class);
                startActivity(guestIntent);
        }
        });

        //direct to menu activity
        Button menuBtn = findViewById(R.id.btnAddMenu);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent menuIntent = new Intent(v.getContext(), CreateEventActivity.class);
                //startActivity(menuIntent);
            }
        });

        //direct to supplier activity
        Button supplierBtn = findViewById(R.id.btnAddSuppliers);
        supplierBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent supplierIntent = new Intent(v.getContext(), CreateEventActivity.class);
                //startActivity(supplierIntent);
            }
        });


        //Event Type Spinner
        eventTypeSpinner = (Spinner) findViewById(R.id.eventType_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.event_type_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(adapter);
        eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String)eventTypeSpinner.getSelectedItem();
                Toast toast = Toast.makeText(CreateEventActivity.this, "You selected " + selectedItem, Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Date
        date = findViewById(R.id.date);
        cal = Calendar.getInstance();
        date.setText(cal.get(Calendar.YEAR) + " / " + (cal.get(Calendar.MONTH) + 1) + " / " + cal.get(Calendar.DATE));

        Button editDate = findViewById(R.id.editDate);

        Calendar calendar = new GregorianCalendar();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();

            }
        });


    }

    @Override
    public void onPause() {
        Log.d(TAG, "'Create Event' Page Paused");
        SharedPreferences.Editor editor = savedValues.edit();

        editor.putString("eventName", eventName.getText().toString());
        editor.putString("eventType", eventTypeSpinner.getSelectedItem().toString());
        editor.putString("date", date.getText().toString());
        editor.putString("address", address.getText().toString());
        //editor.putString("guests", guests.getText().toString());
        //editor.putString("menu", menu.getText().toString());
        //editor.putString("supplies", supplies.getText().toString());
        editor.apply();

        super.onPause();
    }


    @Override
    public void onResume()
    {
        Log.d(TAG, "'Create Event' Page Resumed");
        super.onResume();

        eventName.setText(savedValues.getString("eventName", ""));
        date.setText(savedValues.getString("date", cal.get(Calendar.YEAR) + " / " + (cal.get(Calendar.MONTH) + 1) + " / " + cal.get(Calendar.DATE)));
        address.setText(savedValues.getString("address", ""));
        guests.setText(savedValues.getString("guests", "List of Guests"));           //instead of savedValues, put something from different page
        //menu.setText(savedValues.getString("menu", "List of Menu"));              //instead of savedValues, put something from different page
        //supplies.setText(savedValues.getString("supplies", "List of Supplies"));  //instead of savedValues, put something from different page

    }

    @Override
    public void onStop()
    {
        Log.d(TAG, "'Create Event' Page Stopped");
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        Log.d(TAG, "'Create Event' Page Destroyed");
        super.onDestroy();

    }

    void showDateDialog()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month+1;
                mDay = dayOfMonth;
                Toast.makeText(getApplicationContext(), mYear + " / " + mMonth + " / " + mDay, Toast.LENGTH_SHORT).show();
                date.setText(mYear + " / " + mMonth + " / " + mDay);
            }
        },mYear, mMonth, mDay);

        datePickerDialog.setMessage("Date");
        datePickerDialog.show();
    }



}
