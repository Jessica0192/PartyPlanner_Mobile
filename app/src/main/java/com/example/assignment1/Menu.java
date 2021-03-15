/*
 * FILE          : Menu.java
 * PROJECT       : PROG3150 - Assignment #1
 * PROGRAMMER    : Hoda Akrami
 * FIRST VERSION : 2020-02-10
 * DESCRIPTION   : This file contains the functionality behind the menu.xml file.
 *                 When the user is presented with this screen, he had a list view of menu,
 *                 he can select the type of food that he wants to serve in his party and save it.
 */

package com.example.assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import android.net.Uri;

import java.util.Calendar;

/*
 * NAME     :    Menu
 * PURPOSE :    Menu class contains the functionality behind the menu.xml file.
 *              It contains functions for loading the page, saving the selected items on the shared preferences,
 *              and getting back to the activity page.
 */
public class Menu extends AppCompatActivity {

    private TextView menu = null;
    private CheckBox drink = null;
    private CheckBox appetizer = null;
    private CheckBox mainDish = null;
    private CheckBox dessert = null;
    private Button save = null;
    private SharedPreferences savedValues = null;
    SharedPreferences menuStorage;
    public static final String TAG = "menu";

    //create an instance of database
   // DatabaseHelper menuDb;


    /*  -- Function Header Comment
	Name	:   onCreate()
	Purpose :   This function is called when the page is loaded,
	Inputs	:	Bundle   savedInstanceState - save instance state
	Outputs	:	NONE
	Returns	:	NONE
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        drink = findViewById(R.id.checkbox_drink);
        appetizer = findViewById(R.id.checkbox_appetizer);
        mainDish = findViewById(R.id.checkbox_mainDish);
        dessert = findViewById(R.id.checkbox_dessert);

        //////////////////////////////Passed event id from ViewEventActivity///////////////////////////////////////
        Intent updateMenuIntent = getIntent();
        String eventID = updateMenuIntent.getStringExtra("eventid");
        Log.d(TAG, "'Menu' =========event ID===========" + eventID); // test log msg
        ////////////////////////////////////////////////////////////////////////////////////////

        menuStorage = getSharedPreferences("MenuSelected", Context.MODE_PRIVATE);

        menuStorage.edit().clear().apply();

        Button saveBtn = findViewById(R.id.saveBtn);
        //Applying the Listener on the Button click
        saveBtn.setOnClickListener(new View.OnClickListener() {

            /*  -- Function Header Comment
            Name	:   onClick()
            Purpose :   To save the user selection and store them on shared preferences
                        also, display them in a popup message
            Inputs	:	View     view
            Outputs	:	The popup message to present what items selected
            Returns	:	NONE
            */
            @Override
            public void onClick(View view) {

                StringBuilder result = new StringBuilder();
                //to store the selected items
                //result.append("Selected Items:");
                if (drink.isChecked()) {
                    result.append("drink");
                }
                if (appetizer.isChecked()) {
                    result.append("/");
                    result.append("appetizer");
                }
                if (mainDish.isChecked()) {
                    result.append("/");
                    result.append("mainDish");
                }
                if (dessert.isChecked()) {
                    result.append("/");
                    result.append("dessert");
                }
                //Displaying the message on the toast
                Toast.makeText(getApplicationContext(), "Menu item  selected has been saved! " + result.toString(), Toast.LENGTH_LONG).show();


                //to save the selected items on shared preferences
                SharedPreferences.Editor editor = menuStorage.edit();

//                boolean isInseted = menuDb.insertInfo(result.toString());
//
//                if (isInseted == true)
//                    Toast.makeText (getApplicationContext(), "Menu inserted to the database! ", Toast.LENGTH_LONG).show();
//                else
//                    Toast.makeText (getApplicationContext(), "Menu NOT inserted to the database! ", Toast.LENGTH_LONG).show();

                editor.putString("MenuItems", result.toString());
                editor.apply();
            }
        });
    }

    /*  -- Function Header Comment
    Name	:   onLinkClick(View v)
    Purpose :   To go to the link to make an order
    Inputs	:	View     view
    Outputs	:	NONE
    Returns	:	NONE
    */
    public void onLinkClick(View v)
    {
        // get the intent
        Intent intent = getIntent();

        // get the Uri for the link
        //String link = intent.getStringExtra("link");
        String link = "https://www.sobeys.com/en/";
        Uri viewUri = Uri.parse(link);

        // create the intent and start it
        Intent viewIntent = new Intent(Intent.ACTION_VIEW, viewUri);
        startActivity(viewIntent);

        // This code uses a WebView widget to open the browser within the app
        /*
        Intent out = new Intent(getApplicationContext(), ItemWebView.class);
        out.putExtra("link", in.getStringExtra("link"));
        startActivity(out);
        */
    }

    /*  -- Function Header Comment
    Name	:   backToEvent()
    Purpose :   To let the user back to the event page
    Inputs	:	View     view
    Outputs	:	NONE
    Returns	:	NONE
    */
    public void backToEvent(View view)
    {
        //go back to creation event
        finish();
    }

    /*  -- Function Header Comment
    Name	:   onPause()
    Purpose :   This function is called the current page is in the state of Pause
                and it saves all the data for later use
    Inputs	:	NONE
    Outputs	:	NONE
    Returns	:	NONE
    */
    @Override
    public void onPause() {
        Log.d(TAG, "'Menu' Page Paused");
        super.onPause();
    }

    /*  -- Function Header Comment
    Name	:   onResume()
    Purpose :   This function is called the current page is in the state of Resume
                and it gets all the data which was saved
    Inputs	:	NONE
    Outputs	:	NONE
    Returns	:	NONE
    */
    @Override
    public void onResume()
    {
        Log.d(TAG, "'Menu' Page Resumed");
        super.onResume();
    }

    /*  -- Function Header Comment
    Name	:   onStop()
    Purpose :   This function is called the current page is in the state of Stop
    Inputs	:	NONE
    Outputs	:	NONE
    Returns	:	NONE
    */
    @Override
    public void onStop()
    {
        Log.d(TAG, "'Menu' Page Stopped");
        super.onStop();
    }

    /*  -- Function Header Comment
    Name	:   onDestroy()
    Purpose :   This function is called the current page is in the state of Destroy
    Inputs	:	NONE
    Outputs	:	NONE
    Returns	:	NONE
    */
    @Override
    public void onDestroy()
    {
        Log.d(TAG, "'Menu' Page Destroyed");
        super.onDestroy();
    }


}
