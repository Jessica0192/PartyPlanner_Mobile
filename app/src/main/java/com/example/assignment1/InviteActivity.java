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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
 * FILE          : InviteActivity.java
 * PROJECT       : PROG3150 - Assignment #1
 * PROGRAMMER    : Maria Malinina
 * FIRST VERSION : 2020-02-10
 * DESCRIPTION   :
 * This file contains the functionality behind the content_invite.xml screen.
 * This is where the user selected the guest he wants to invite to the party
 * and is going to send invitation to. Here, before sending the invitation, the
 * user can specify the event name, description, date, full address, after which
 * he can either send the invitation or go back to the guest list screen.
 * In this file we have functionality to retrieve the shared preferences variable
 * with the guest we want to send invitation to, different UI handling and simple
 * on-click events for the button to send invitation and going back to the guest
 * list.
 */

public class InviteActivity extends AppCompatActivity {

    //TAG variable indicating the current activity
    private static final String TAG = "InviteActivity";

    //shared preferences object to store the guests we sent invitations to
    SharedPreferences shared_obj_guests;

    //to keep track of the guest we are currently sending invitation to
    String curGuest;
    //text view object to display the date
    private TextView displayDate;

    //dropdown list object
    private Spinner spinner;

    //date picker object to make it easier for the user
    // to choose the date
    private DatePickerDialog.OnDateSetListener dateListener;


    /*
     * FUNCTION   : onCreate()
     * DESCRIPTION: This function is called when the page is loaded, so here
     * we have some basic functionality of the page where the user can fill in
     * the party name, description, select a date of the event, fill in the address.
     * PARAMETERS : Bundle savedInstanceState
     * RETURNS    : NONE
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the content that we are going to see on the
        //content_invite screen
        setContentView(R.layout.content_invite);


        //get the spinner from the xml.
        spinner = findViewById(R.id.provinces_dropdown);

        //establish a new list object
        List<String> provinces = new ArrayList<>();

        //add all Candian provinces to the list array
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

        //set an array adapter to adapt an array containing Canadian provinces to
        // the dropdown list that we have
        ArrayAdapter<String> dataAdapter;
        //set the adapter to the provinces array
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces);

        //set the dropdown list to display the provinces
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //now the dropdown is displaying the provinces
        spinner.setAdapter(dataAdapter);

        //add a listener to make sure the dropdown is handled appropriately
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /*
             * FUNCTION   : onItemSelected()
             * DESCRIPTION: If we select something from the dropdown, we need
             * it to be displayed after we close the dropdown.
             * PARAMETERS : AdapterView<?> parent,
             *              View view,
             *              int position,
             *              long id
             * RETURNS    : NONE
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose Province"))
                {
                    String item = parent.getItemAtPosition(position).toString();

                }
            }

            /*
             * FUNCTION   : onNothingSelected()
             * DESCRIPTION: The listener we implemented requires this
             * method to be overriden.
             * PARAMETERS : AdapterView<?> parent
             * RETURNS    : NONE
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //now we need to handle the date selection
        displayDate = (TextView) findViewById(R.id.selectDate);

        //handle data selection
        displayDate.setOnClickListener(new View.OnClickListener(){
            /*
             * FUNCTION   : onClick()
             * DESCRIPTION: When the user clicks on "Select date",
             * we need to provide him with scrolling view where he
             * can choose the year, date, month in a spinner-like
             * view, which is convenient and fast for the user to
             * pick a date.
             * PARAMETERS : View view
             * RETURNS    : NONE
             */
            @Override
            public void onClick(View view){
                //get a calendar instance
                Calendar cal = Calendar.getInstance();

                //get the year from the calendar
                int year = cal.get(Calendar.YEAR);
                //get the month from the calendar
                int month = cal.get(Calendar.MONTH);
                //get the day from the calendar
                int day = cal.get(Calendar.DAY_OF_MONTH);

                //establish a new instance of date picking dialog
                DatePickerDialog dialog = new DatePickerDialog(
                        InviteActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateListener,
                        year, month, day
                );

                //set the dialog's appearance
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //show the dialog
                dialog.show();
            }
        });

        //establish a new dateListener object
        dateListener = new DatePickerDialog.OnDateSetListener(){
            /*
             * FUNCTION   : onDateSet()
             * DESCRIPTION: Display the date that the user picked and
             * log the date the user picked.
             * PARAMETERS : DatePicker datePicker,
             *              int year,
             *              int month,
             *              int day
             * RETURNS    : NONE
             */
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day){
                month = month + 1;
                //log the date the user chose
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                //string object storing the date user chose
                String date = month + "/" + day + "/" + year;
                //display what user chose on the screen
                displayDate.setText(date);
            }
        };



        //a new text view object
        TextView t1;
        //find the text view where we put the name of the guest who
        // we are currently sending invitation to by id
        t1= findViewById(R.id.whoToInvite);

        //get the shared preferences object called "GuestPrefs" that filled in in the GuestActivity
        //while selecting who to possibly send invitation to
        SharedPreferences sp = getApplicationContext().getSharedPreferences("GuestPrefs", Context.MODE_PRIVATE);
        //get the cur_guest value that we filled in in GuestActivity
        curGuest = sp.getString("cur_guest","");

        //if cur_guest is null
        if (curGuest==null)
        {
            curGuest="NULL";
        }

        // show who the user is attempting to send invitation
        // to on the screen
        t1.setText("Invitation to: "+curGuest);

        //find the button that sends the invitation by id
        Button button = findViewById(R.id.sendBtn);

        //set a listener when we click on item to handle the on click event
        button.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION   : onClick()
             * DESCRIPTION: This function is to inform the user his
             * invitation has been sent to the guest and to save the
             * invited guests.
             * PARAMETERS : View view
             * RETURNS    : NONE
             */
            @Override
            public void onClick(View view) {
                //a new editor object to edit the contents of the "GuestPrefs"
                SharedPreferences.Editor editor = sp.edit();

                // fil the "GuestPrefs" object with all the possible guests -
                // we allow up to 5 guests
                if (sp.getString("guest1", "") == ""){
                     editor.putString("guest1", curGuest);
                }else if (sp.getString("guest2", "") == ""){
                    editor.putString("guest2", curGuest);
                } else if (sp.getString("guest3", "") == ""){
                    editor.putString("guest3", curGuest);
                }else if (sp.getString("guest4", "") == ""){
                    editor.putString("guest4", curGuest);
                }else if (sp.getString("guest5", "") == ""){
                    editor.putString("guest5", curGuest);
                }

                //apply changes
                editor.apply();

                //inform the user that the invitation has been sent
                Toast.makeText(InviteActivity.this, "Invitation sent", Toast.LENGTH_SHORT).show();

            }
        });

    }


    /*
     * FUNCTION   : backToList()
     * DESCRIPTION: This is a simple an on-click
     * event for the situation when the user decided to
     * go back to the list of possible guests, so we are
     * just redirecting to the screen of possible guests.
     * PARAMETERS : View view
     * RETURNS    : NONE
     */
    public void backToList(View view)
    {
        //go back to the guests list screen
        Intent intent = new Intent(this, GuestActivity.class);
        startActivity(intent);
    }
}

