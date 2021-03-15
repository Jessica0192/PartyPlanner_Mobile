/*
 *   FILE: CreateEventActivity.java
 *   Project: A1
 *   PROGRAMMER: Jessica Sim
 *   FIRST VERSION: 2021-02-12
 *   DESCRIPTION:
 *	    This file contains the logic of creating new event. It takes event name, event type, date, address, name of guests, menus
 *      and supplies. If user chooses to press buttons for adding name of guests, selecting menus and supplies, it directs to
 *      the appropriate page. When user press SAVE button below, it creates a new event with the information
 *      that is given.
 */
package com.example.assignment1;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
/*
 * NAME     :    CreateEventActivity
 * PURPOSE :    CreateEventActivity class contains the functionality behind the activity_create_event.xml file.
 *              It contains functions for taking event name, event type, date, address, name of guests, menus
 *              and supplies.
 */
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
    private SharedPreferences menuValues = null;

    SQLiteOpenHelper dbHelper = null;
    SQLiteDatabase db = null;

    PartyPlannerDB dbhelper = new PartyPlannerDB(CreateEventActivity.this);

    /*
     * FUNCTION: onCreate
     * DESCRIPTION:
     *      This function is going to be called as default when this page is loaded and there are
     *      several functions and listeners that does some actions
     * PARAMETER:
     *      Bundle savedInstanceState: save instance state
     * RETURNS:
     *      void: there's no return value
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        savedValues = getSharedPreferences("Saved Values", MODE_PRIVATE);
        eventName = findViewById(R.id.txtEventName);

        eventName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    hideKeyboard((EditText) eventName);
                    return true;
                }
                return false;
            }
        });

        address = findViewById(R.id.editAddress);


        address.setOnKeyListener(new View.OnKeyListener() {
            /*
             * FUNCTION: onKey
             * DESCRIPTION:
             *      This function is for hiding the keyboard when ENTER button is clicked
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             *      int keyCode: this represents which key is pressed
             *      KeyEvent keyEvent: it has several events related to keyevent
             * RETURNS:
             *      boolean: return true or false depends on the result
             */
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    hideKeyboard((EditText) address);
                    return true;
                }
                return false;
            }
        });

        guests = findViewById(R.id.listOfGuests);
        menu = findViewById(R.id.txtMenu);
        supplies = findViewById(R.id.txtSupplies);

        //go back to events page
        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when SAVE button is clicked
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "Create Event Activity -- on click triggered");
                SharedPreferences.Editor editor = savedValues.edit();

                if(eventName.getText().toString().matches("") ||
                        eventTypeSpinner.getSelectedItem().toString().matches("") ||
                        date.getText().toString().matches("") ||
                        address.getText().toString().matches("") ||
                        guests.getText().toString().matches("List of Guests"))
                {
                    Toast.makeText(getApplicationContext(), "Please provide all the necessary fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG, "Create Event Activity -- put string");

                //editor
                editor.putString("eventName", eventName.getText().toString());
                editor.putString("eventType", eventTypeSpinner.getSelectedItem().toString());
                editor.putString("date", date.getText().toString());
                editor.putString("address", address.getText().toString());
                editor.putString("guests", guests.getText().toString());
                editor.putString("menu", menu.getText().toString());
                editor.putString("supplies", supplies.getText().toString());
                editor.apply();

                //db = dbHelper.getWritableDatabase();

                StringBuilder guestsls = new StringBuilder();
                SharedPreferences suppliesSp = getApplicationContext().getSharedPreferences("SupplySelected", Context.MODE_PRIVATE);
                SharedPreferences menuSp = getApplicationContext().getSharedPreferences("MenuSelected", Context.MODE_PRIVATE);
                SharedPreferences guestSp = getApplicationContext().getSharedPreferences("GuestPrefs", Context.MODE_PRIVATE);
                if (!guestSp.getString("guest1", "").equals("")){
                    guestsls.append(guestSp.getString("guest1", ""));
                }
                if (!guestSp.getString("guest2", "").equals("")){
                    guestsls.append("/");
                    guestsls.append(guestSp.getString("guest2", ""));

                }
                if (!guestSp.getString("guest3", "").equals("")){
                    guestsls.append("/");
                    guestsls.append(guestSp.getString("guest3", ""));
                }
                if (!guestSp.getString("guest4", "").equals("")){
                    guestsls.append("/");
                    guestsls.append(guestSp.getString("guest4", ""));
                }
                if (!guestSp.getString("guest5", "").equals("")){
                    guestsls.append("/");
                    guestsls.append(guestSp.getString("guest5", ""));
                }

                //insert all data values in plannerInfo
                //ContentValues values= new ContentValues();


                Log.d(TAG, eventName.getText().toString());
                Log.d(TAG, eventTypeSpinner.getSelectedItem().toString());
                Log.d(TAG, date.getText().toString());
                Log.d(TAG, guestsls.toString());
                Log.d(TAG, menu.getText().toString());
                Log.d(TAG, supplies.getText().toString());


                //values.put("eventName", eventName.getText().toString());
                //values.put("eventType", eventTypeSpinner.getSelectedItem().toString());
                //values.put("date", date.getText().toString());
                //values.put("address", address.getText().toString());
                //values.put("guests", guestsls.toString());
                //values.put("menu", menuSp.getString("MenuItems", ""));
                //values.put("supplies", suppliesSp.getString("SupplyItems", ""));


                dbhelper.insertEvent(eventName.getText().toString(), eventTypeSpinner.getSelectedItem().toString(), date.getText().toString(),
                        address.getText().toString(), guestsls.toString(), menuSp.getString("MenuItems", "No values"),
                        suppliesSp.getString("SupplyItems", "No values"));
                //dbhelper.insertEvent("1", "2", "3", "4", "5", "6", "7");

                //db.insert("plannerInfo", null, values);

                finish();
            }
        });



        addGuests = findViewById(R.id.btnAddGuests);
        addGuests.setOnClickListener(new View.OnClickListener(){
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when addGuest button is clicked and it directs to GuestActivity
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v){
                Intent guestIntent = new Intent(CreateEventActivity.this, GuestActivity.class);
                startActivity(guestIntent);
        }
        });

        //direct to menu activity
        Button menuBtn = findViewById(R.id.btnAddMenu);
        menuBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when menu button is clicked and it directs to MenuActivity
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v) {
                Intent menuIntent = new Intent(v.getContext(), Menu.class);
                startActivity(menuIntent);
            }
        });

        //direct to supplier activity
        Button suppliesBtn = findViewById(R.id.btnAddSuppliers);
        suppliesBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when choosing supplies button is clicked and it directs to SuppliesActivity
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v) {
                Intent supplierIntent = new Intent(v.getContext(), Supplies.class);
                startActivity(supplierIntent);
            }
        });


        //Event Type Spinner
        eventTypeSpinner = (Spinner) findViewById(R.id.eventType_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.event_type_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(adapter);
        eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /*
             * FUNCTION: onItemSelected
             * DESCRIPTION:
             *      This function is called when one of the item in dropdown list is clicked
             * PARAMETER:
             *      AdapterView<?> parent: AdapterView where the selection happened
             *      View v: the view within the AdapterView that was clicked
             *      int position: position of the view in adapter
             *      long id: row id of the item that is selected
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String)eventTypeSpinner.getSelectedItem();
                Toast toast = Toast.makeText(CreateEventActivity.this, "You selected " + selectedItem, Toast.LENGTH_SHORT);
                toast.show();
            }

            /*
             * FUNCTION: onNothingSelected
             * DESCRIPTION:
             *      This function is called when nothing is selected in dropdown list
             * PARAMETER:
             *      AdapterView<?> parent: AdapterView where the selection happened
             * RETURNS:
             *      void: there's no return value
             */
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
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when editDate button is clicked and this function calls another function named,
             *      "showDateDialog" to show the date dialog
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v) {
                showDateDialog();

            }
        });


    }


    /*
     * FUNCTION: onPause
     * DESCRIPTION:
     *      This function is called the current page is in the state of Pause and it saves all the data
     *      for later use
     * PARAMETER:
     *      there's no parameter
     * RETURNS:
     *      void: there's no return value
     */
    @Override
    public void onPause() {
        Log.d(TAG, "'Create Event' Page Paused");
        SharedPreferences.Editor editor = savedValues.edit();


        editor.putString("eventName", eventName.getText().toString());
        editor.putString("eventType", eventTypeSpinner.getSelectedItem().toString());
        editor.putString("date", date.getText().toString());
        editor.putString("address", address.getText().toString());
        editor.putString("guests", guests.getText().toString());
        editor.putString("menu", menu.getText().toString());
        editor.putString("supplies", supplies.getText().toString());
        editor.apply();

        super.onPause();
    }


    /*
     * FUNCTION: onResume
     * DESCRIPTION:
     *      This function is called the current page is in the state of Resume and it gets all the data
     *      which was saved
     * PARAMETER:
     *      there's no parameter
     * RETURNS:
     *      void: there's no return value
     */
    @Override
    public void onResume()
    {
        Log.d(TAG, "'Create Event' Page Resumed");
        StringBuilder guestsls = new StringBuilder();
        SharedPreferences suppliesSp = getApplicationContext().getSharedPreferences("SupplySelected", Context.MODE_PRIVATE);
        SharedPreferences menuSp = getApplicationContext().getSharedPreferences("MenuSelected", Context.MODE_PRIVATE);
        SharedPreferences guestSp = getApplicationContext().getSharedPreferences("GuestPrefs", Context.MODE_PRIVATE);
        if (!guestSp.getString("guest1", "").equals("")){
            guestsls.append(guestSp.getString("guest1", ""));
            guestsls.append("\n");
        }
        if (!guestSp.getString("guest2", "").equals("")){
            guestsls.append(guestSp.getString("guest2", ""));
            guestsls.append("\n");
        }
        if (!guestSp.getString("guest3", "").equals("")){
            guestsls.append(guestSp.getString("guest3", ""));
            guestsls.append("\n");
        }
        if (!guestSp.getString("guest4", "").equals("")){
            guestsls.append(guestSp.getString("guest4", ""));
            guestsls.append("\n");
        }
        if (!guestSp.getString("guest5", "").equals("")){
            guestsls.append(guestSp.getString("guest5", ""));
            guestsls.append("\n");
        }
        super.onResume();

        eventName.setText(savedValues.getString("eventName", ""));
        date.setText(savedValues.getString("date", cal.get(Calendar.YEAR) + " / " + (cal.get(Calendar.MONTH) + 1) + " / " + cal.get(Calendar.DATE)));
        address.setText(savedValues.getString("address", ""));
        guests.setText(guestsls.toString());           //instead of savedValues, put something from different page
        menu.setText(menuSp.getString("MenuItems", ""));              //instead of savedValues, put something from different page
        supplies.setText(suppliesSp.getString("SupplyItems", ""));  //instead of savedValues, put something from different page

    }

    /*
     * FUNCTION: onStop
     * DESCRIPTION:
     *      This function is called the current page is in the state of Stop
     * PARAMETER:
     *      there's no parameter
     * RETURNS:
     *      void: there's no return value
     */
    @Override
    public void onStop()
    {
        Log.d(TAG, "'Create Event' Page Stopped");
        super.onStop();
    }

    /*
     * FUNCTION: onDestroy
     * DESCRIPTION:
     *      This function is called the current page is in the state of Destroy
     * PARAMETER:
     *      there's no parameter
     * RETURNS:
     *      void: there's no return value
     */
    @Override
    public void onDestroy()
    {
        Log.d(TAG, "'Create Event' Page Destroyed");
        super.onDestroy();
    }

    /*
     * FUNCTION: showDateDialog
     * DESCRIPTION:
     *      This function is called to show the date dialog and allow user to choose the date
     * PARAMETER:
     *      there's no parameter
     * RETURNS:
     *      void: there's no return value
     */
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

    /*
     * FUNCTION: hideKeyboard
     * DESCRIPTION:
     *      This function is called to hide the keyboard
     * PARAMETER:
     *      EditText et: EditText widget is passed
     * RETURNS:
     *      void: there's no return value
     */
    private void hideKeyboard(EditText et)
    {
        InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

}
