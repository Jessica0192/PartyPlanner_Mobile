/*
 * FILE          : SettingsActivity.java
 * PROJECT       : PROG3150 - Assignment #1
 * PROGRAMMER    : Suka Sun
 * FIRST VERSION : 2021-04-21
 * DESCRIPTION   : This file contains the functionality behind the activity_settings.xml file.
 *                 The user can set the time format
 */

package com.example.assignment1;

import android.app.Activity;
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
 * NAME     :   SettingsActivity
 * PURPOSE :    EventListActivity class contains the functionality behind the activity_event_list.xml file.
 *              It contains functions for loading the page, saving the submitted values on the shared preferences,
 *              and updating page after each event creation.
 */
public class SettingsActivity extends Activity {
    public static final String TAG = "SettingsActivity";
    private TextView timeFormat=null;
    Button btn12hr = null;
    Button btn24hr = null;
    Button btnExit = null;

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
        Log.d(TAG, "'SettingsActivity' Page onCreate");

//        // Retrieve value from shared preferences
//        savedEvents = getSharedPreferences("Saved Events", MODE_PRIVATE);
//        savedValues = getSharedPreferences("Saved Values", MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        timeFormat = findViewById(R.id.txtEventItem);


        // 12 hrs format
        btn12hr = (Button) findViewById(R.id.btn12hr);
        btn12hr.setOnClickListener(new View.OnClickListener() {
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
                SharedPreferences clockSettings = getSharedPreferences("TimeFormat", Context.MODE_PRIVATE);
                clockSettings.edit().putString("TimeFormat", "12").apply();
                finish();
            }
        });

        // 24 hrs format
        btn24hr = (Button) findViewById(R.id.btn24hr);
        btn24hr.setOnClickListener(new View.OnClickListener() {
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
                SharedPreferences clockSettings = getSharedPreferences("TimeFormat", Context.MODE_PRIVATE);
                clockSettings.edit().putString("TimeFormat", "24").apply();
                finish();
            }
        });

        // Exit
        btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
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
                finish();
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
        Log.d(TAG, "'SettingsActivity' Page Paused");
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
        Log.d(TAG, "'SettingsActivity' Page Resumed");
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
        Log.d(TAG, "'SettingsActivity' Page Stopped");
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
        Log.d(TAG, "'SettingsActivity' Page Destroyed");
        super.onDestroy();
    }
}
