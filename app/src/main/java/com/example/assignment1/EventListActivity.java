package com.example.assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


/*
 * NAME     :   EventListActivity
 * PURPOSE :    EventListActivity class contains the functionality behind the activity_event_list.xml file.
 *              It contains functions for loading the page, saving the submitted values on the shared preferences,
 *              and updating page after each event creation.
 */
public class EventListActivity extends MainActivity {

    public static final String TAG = "EventListActivity";
    private TextView eventItem=null;
    private SharedPreferences savedEvents;
    private String eventNameList;
    private String eventDateList;
    private String eventname = "";
    private String eventdate = "";
    private SharedPreferences savedValues;
    Button addEventBtn = null;
    Button clearEventsBtn = null;

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
        // Retrieve value from shared preferences
        savedEvents = getSharedPreferences("Saved Events", MODE_PRIVATE);
        savedValues = getSharedPreferences("Saved Values", MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        eventItem = findViewById(R.id.txtEventItem);
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
                // Retrieve date from shared preferences and clear data
                getApplicationContext().getSharedPreferences("GuestPrefs", Context.MODE_PRIVATE).edit().clear().apply();
                getApplicationContext().getSharedPreferences("MenuSelected", Context.MODE_PRIVATE).edit().clear().apply();;
                getApplicationContext().getSharedPreferences("SupplySelected", Context.MODE_PRIVATE).edit().clear().apply();;
                getApplicationContext().getSharedPreferences("Saved Values", Context.MODE_PRIVATE).edit().clear().apply();;
                startActivity(createEventIntent);
            }
        });
        // Clear events
        clearEventsBtn = (Button) findViewById(R.id.btnClearEvents);
        clearEventsBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when Clear Events button is clicked
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v) {
                // Retrieve date from shared preferences and clear data
                getApplicationContext().getSharedPreferences("GuestPrefs", Context.MODE_PRIVATE).edit().clear().apply();
                getApplicationContext().getSharedPreferences("MenuSelected", Context.MODE_PRIVATE).edit().clear().apply();;
                getApplicationContext().getSharedPreferences("SupplySelected", Context.MODE_PRIVATE).edit().clear().apply();;
                getApplicationContext().getSharedPreferences("Saved Values", Context.MODE_PRIVATE).edit().clear().apply();;
                getApplicationContext().getSharedPreferences("Saved Events", Context.MODE_PRIVATE).edit().clear().apply();
                // Refresh the page
                startActivity(new Intent(v.getContext(), EventListActivity.class));
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
        // Display data in text view
        eventname = savedValues.getString("eventName", "");
        eventdate = savedValues.getString("date", "");
        eventNameList = savedEvents.getString("eventNameList", "");
        eventDateList = savedEvents.getString("eventDateList", "");
        // Handle the case when there is no event name entered yet
        if (eventname.equals(""))
        {
            // Handle the case when there is no saved event name entered yet
            if (eventNameList.equals(""))
            {
                // Display default message
                eventItem.setText("There is no event yet");
            }
            // Handle the case when the event name is not empty
            else
            {
                // Display saved data
                eventItem.setText(eventNameList + " / " + eventDateList);
            }
        }
        // Handle the case when there is newly entered event name
        else
        {
            SharedPreferences.Editor editor = savedEvents.edit();
            // Handle the case when there is no saved data yet
            if (eventNameList.equals(""))
            {
                // Add entered data
                editor.putString("eventNameList", eventname);
                editor.putString("eventDateList", eventdate);
            }
            // Handle the case when there is saved data yet
            else
            {
                // Append entered data
                editor.putString("eventNameList", eventNameList + ";" + eventname);
                editor.putString("eventDateList", eventDateList + ";" + eventdate);
            }
            editor.apply();
            eventNameList = savedEvents.getString("eventNameList", "");
            eventDateList = savedEvents.getString("eventDateList", "");
            // Store data to string array with a separator
            String[] myDataNameList = eventNameList.split(";");
            String[] myDataDateList = eventDateList.split(";");
            String tmp = "";

            for (int i = 0; i < myDataNameList.length; i++) {
                // Handle the case when string is empty
                if ( tmp == "")
                {
                    tmp = myDataNameList[i] + " : " +  myDataDateList[i];
                }
                // Handle the case when string is not empty
                else
                {
                    tmp = tmp + ";" + myDataNameList[i] + " : " +  myDataDateList[i];
                }
            }
            // Display data
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