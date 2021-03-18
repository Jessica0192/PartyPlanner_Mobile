/*
 * FILE          : ViewEventActivity.java
 * PROJECT       : PROG3150 - Assignment #2
 * PROGRAMMER    : Suka Sun
 * FIRST VERSION : 2020-03-19
 * DESCRIPTION   : This file contains the functionality behind the activity_view_delete_events.xml file.
 *                 The user can select an event, view event details, updated information for guests,
 *                 menu items, and supply items, and delete the event.
 */

package com.example.assignment1;

import android.app.slice.SliceItem;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.ArrayList;


/*
 * NAME     :   ViewEventActivity
 * PURPOSE :    ViewEventActivity class contains the functionality behind the activity_view_delete_events.xml file.
 *              It contains functions for loading the page, saving the submitted values on the shared preferences,
 *              and updating page after each event creation.
 */

public class ViewEventActivity extends EventListActivity {

    public static final String TAG = "EventListActivity";
    private TextView eventDetails = null;
    private EditText eventID = null;
    Button viewDetailsBtn = null;
    Button deleteEventBtn = null;
    Button backBtn = null;
    Button updateGuestBtn = null;
    Button updateMenuBtn = null;
    Button updateSupplyBtn = null;
    // Constants for error code
    private static final int ERROR_EMPTY =  -1;
    private static final int ERROR_INVALID =  -2;


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
        Log.d(TAG, "'View Event' Page onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_delete_events);
        eventDetails = findViewById(R.id.txtEventDetails);
        eventID = findViewById(R.id.txtEventID);

        eventID.setOnKeyListener(new View.OnKeyListener() {
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
                    hideKeyboard((EditText) eventID);
                    return true;
                }
                return false;
            }
        });

        // View event details
        viewDetailsBtn = (Button) findViewById(R.id.btnShowDetails);
        viewDetailsBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when View Details button is clicked
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v) {
                PartyPlannerDB db = new PartyPlannerDB(v.getContext());
                String value= eventID.getText().toString();
                int eventNum=Integer.parseInt(value) - 1;
                String tmp = db.getEventDetails(eventNum);
                Log.d(TAG, "eventNum: "+eventNum);
                Log.d(TAG, tmp);
                if ( tmp == "<NO DATA>")
                {
                    eventDetails.setText("There is no event yet");
                }
                else if ( tmp == "<INVALID ID>" )
                {
                    Toast.makeText(getApplicationContext(), "Invalid Event ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    eventDetails.setText(tmp);
                }
            }
        });

        // Delete event
        deleteEventBtn = (Button) findViewById(R.id.btnDeleteEvent);
        deleteEventBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when Delete Event button is clicked
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v) {
                PartyPlannerDB db = new PartyPlannerDB(v.getContext());
                PartyMenuDB dbMenu= new PartyMenuDB(v.getContext());
                PartySupplyDB dbSupply= new PartySupplyDB(v.getContext());
                PartyGuestDB dbGuest= new PartyGuestDB(v.getContext());
                String value= eventID.getText().toString();
                Log.d(TAG, "value: "+value);
                String eventNum=String.valueOf(Integer.parseInt(value) - 1);
                int tmp = db.deleteEvent(eventNum);
                dbMenu.deleteMenuItem(eventNum);
                dbSupply.deleteSupply(eventNum);
                dbGuest.deleteGuest(eventNum);
                Log.d(TAG, String.valueOf(tmp));
                if(tmp == ERROR_EMPTY)
                {
                    Toast.makeText(getApplicationContext(), "There is no event yet", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(tmp == ERROR_INVALID)
                {
                    Toast.makeText(getApplicationContext(), "Invalid Event ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Event has been deleted", Toast.LENGTH_SHORT).show();
                    Intent viewEventIntent = new Intent(v.getContext(), ViewEventActivity.class);
                    startActivity(viewEventIntent);
                }

            }
        });

        // Back to Event List
        backBtn = (Button) findViewById(R.id.btnBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when Back to Events button is clicked
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v) {
//                finish();
                Intent eventListIntent = new Intent(v.getContext(), EventListActivity.class);
                startActivity(eventListIntent);
            }
        });

        // Update Menu
        updateMenuBtn = (Button) findViewById(R.id.btnUpdateMenu);
        updateMenuBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when Update Menu button is clicked
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v) {
                Intent updateMenuIntent = new Intent(ViewEventActivity.this, Menu.class);
                String value= eventID.getText().toString();
                String eventNum=String.valueOf(Integer.parseInt(value) - 1);
                updateMenuIntent.putExtra("eventID", eventNum);
                startActivity(updateMenuIntent);
            }
        });

        // Update Supply
        updateSupplyBtn = (Button) findViewById(R.id.btnUpdateSupply);
        updateSupplyBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when Update Supply button is clicked
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v) {
                Intent updateSupplyIntent = new Intent(ViewEventActivity.this, Supplies.class);
                String value= eventID.getText().toString();
                String eventNum=String.valueOf(Integer.parseInt(value) - 1);
                updateSupplyIntent.putExtra("eventID", eventNum);
                startActivity(updateSupplyIntent);
            }
        });

        // Update Guests
        updateGuestBtn = (Button) findViewById(R.id.btnUpdateGuests);
        updateGuestBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when Update Guest button is clicked
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v) {
                Intent updateGuestIntent = new Intent(ViewEventActivity.this, GuestActivity.class);
//                Intent updateGuestInviteIntent = new Intent(ViewEventActivity.this, InviteActivity.class);
                String value= eventID.getText().toString();
                String eventNum=String.valueOf(Integer.parseInt(value) - 1);
                updateGuestIntent.putExtra("eventID", eventNum);
//                updateGuestInviteIntent.putExtra("eventID", eventNum);
                startActivity(updateGuestIntent);
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
        // Log state of the page
        Log.d(TAG, "'View Event' Page Paused");
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
        Log.d(TAG, "'View Event' Page Resumed");
        super.onResume();
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
        Log.d(TAG, "'View Event' Page Stopped");
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
        Log.d(TAG, "'View Event' Page Destroyed");
        super.onDestroy();
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