/*
 * FILE          : ViewEventActivity.java
 * PROJECT       : PROG3150 - Assignment #2
 * PROGRAMMER    : Suka Sun
 * FIRST VERSION : 2020-03-19
 * DESCRIPTION   : This file contains the functionality behind the activity_view_delete_events.xml file.
 *                 The user can select an event, view the event details or delete the event.
 */

package com.example.assignment1;

import android.app.slice.SliceItem;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
                if ( tmp == "<NO DATA>")
                {
                    eventDetails.setText("There is no event yet");
                }
                else
                {
                    eventDetails.setText(tmp);
                }
//                getApplicationContext().getSharedPreferences("Saved Values", Context.MODE_PRIVATE).edit().clear().apply();
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