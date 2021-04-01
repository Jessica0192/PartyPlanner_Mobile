/*
 * FILE          : EventListActivity.java
 * PROJECT       : PROG3150 - Assignment #1
 * PROGRAMMER    : Suka Sun
 * FIRST VERSION : 2020-03-19
 * DESCRIPTION   : This file contains the functionality behind the activity_event_list.xml file.
 *                 The user can view summary of existing events and choose to add new event
 */

package com.example.assignment1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.ArrayList;


/*
 * NAME     :   EventListActivity
 * PURPOSE :    EventListActivity class contains the functionality behind the activity_event_list.xml file.
 *              It contains functions for loading the page, saving the submitted values on the shared preferences,
 *              and updating page after each event creation.
 */
public class EventListActivity extends MainActivity {

    public static final String TAG = "EventListActivity";
    private TextView eventItem=null;
    Button viewEventBtn = null;
    Button addEventBtn = null;

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
    protected void onCreate(Bundle savedInstanceState) {
        // Log state of the page
        Log.d(TAG, "'Event List' Page onCreate");

//        // Retrieve value from shared preferences
//        savedEvents = getSharedPreferences("Saved Events", MODE_PRIVATE);
//        savedValues = getSharedPreferences("Saved Values", MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        eventItem = findViewById(R.id.txtEventItem);

//        Intent eventListIntent = getIntent();
//        String username = eventListIntent.getStringExtra("username");
//        Log.d(TAG, "'Event List' =========username===========" + username);

        // View event
        viewEventBtn = (Button) findViewById(R.id.btnViewEvent);
        viewEventBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when View Event Details button is clicked
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v) {
                Intent viewEventIntent = new Intent(v.getContext(), ViewEventActivity.class);
                startActivity(viewEventIntent);

            }
        });

        // Add event
        addEventBtn = (Button) findViewById(R.id.btnAddEvent);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when Add Event button is clicked
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v) {
                Intent createEventIntent = new Intent(v.getContext(), CreateEventActivity.class);
                startActivity(createEventIntent);
            }
        });
    }


    /*
     * FUNCTION: onCreateOptionsMenu
     * DESCRIPTION:
     *      This function is called to show the menu fragment
     * PARAMETER:
     *      Menu menu: get Menu instance
     * RETURNS:
     *      boolean: returns if succeeded to create menu fragment
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu1,  menu);
        return super.onCreateOptionsMenu( menu);
    }

    /*
     * FUNCTION: onOptionsItemSelected
     * DESCRIPTION:
     *      This function is called when one of the options is selected in the menu
     * PARAMETER:
     *      MenuItem item: get Menu item instance
     * RETURNS:
     *      boolean: returns if succeeded
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.logout:
                Intent logOut = new Intent(EventListActivity.this, MainActivity.class);
                startActivity(logOut);
                break;

            case R.id.about:
                //show about information
                new AlertDialog.Builder(this)
                        .setTitle("About")
                        .setMessage("\nThis is Party Planner application where you can plan and " +
                                "create exciting events!!!\n\n Have Fun:)\n")
                        .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                break;
            case R.id.contact:
                Intent contact = new Intent(EventListActivity.this, ContactActivity.class);
                startActivity(contact);
                break;
        }

        return super.onOptionsItemSelected(item);
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
        // Log state of the page
        Log.d(TAG, "'Event List' Page Paused");
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
        // Log state of the page
        Log.d(TAG, "'Event List' Page Resumed");
        super.onResume();

        // Display data
        PartyPlannerDB db = new PartyPlannerDB(this);
//        PartyMenuDB dbMenu = new PartyMenuDB(this);
//        PartySupplyDB dbSupply = new PartySupplyDB(this);
//        PartyGuestDB dbGuest = new PartyGuestDB(this);
        String tmp = db.getFormattedEventsSummary();
        if ( tmp == "<NO DATA>")
        {
            eventItem.setText("There is no event yet");
        }
        else
        {
            eventItem.setText(tmp.replace(";", System.getProperty("line.separator")));
        }
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
        // Log state of the page
        Log.d(TAG, "'Event List' Page Stopped");
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
        // Log state of the page
        Log.d(TAG, "'Event List' Page Destroyed");
        super.onDestroy();
    }
}