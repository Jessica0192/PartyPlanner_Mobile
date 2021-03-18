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
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
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
    private String eventID = "";
    private String myMenuUpdateItems = "";
    SharedPreferences menuStorage;
    public static final String TAG = "menu";

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
        eventID = updateMenuIntent.getStringExtra("eventID");
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
                if (drink.isChecked())
                {
                    result.append("drink");
                }
                if (appetizer.isChecked())
                {
                    result.append("/");
                    result.append("appetizer");
                }
                if (mainDish.isChecked())
                {
                    result.append("/");
                    result.append("mainDish");
                }
                if (dessert.isChecked())
                {
                    result.append("/");
                    result.append("dessert");
                }
                //Displaying the message on the toast
                //Toast.makeText(getApplicationContext(), "Menu item  selected has been saved! " + result.toString(), Toast.LENGTH_LONG).show();

                //to save the selected items on shared preferences
                SharedPreferences.Editor editor = menuStorage.edit();

                editor.putString("MenuItems", result.toString());
                editor.apply();

                // call the task
                Menu.Task_for_menu mytask= new Menu.Task_for_menu(Menu.this);
                mytask.execute();
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
    Name	:   backToEventDetails(View view)
    Purpose :   To let the user back to the event details page
    Inputs	:	View     view
    Outputs	:	NONE
    Returns	:	NONE
    */
    public void backToEventDetails(View view)
    {
        Intent eventDetailsIntent = new Intent(view.getContext(), ViewEventActivity.class);
        startActivity(eventDetailsIntent);
    }

    /*  -- Function Header Comment
    Name	:   saveUpdate(View view)
    Purpose :   To save the update
    Inputs	:	View     view
    Outputs	:	NONE
    Returns	:	NONE
    */
    public void saveUpdate(View view)
    {
        if (drink.isChecked()) {
            myMenuUpdateItems = ( myMenuUpdateItems == "") ? "drink" : myMenuUpdateItems + ",drink";
        }
        if (appetizer.isChecked()) {
            myMenuUpdateItems = ( myMenuUpdateItems == "") ? "appetizer" : myMenuUpdateItems + ",appetizer";
        }
        if (mainDish.isChecked()) {
            myMenuUpdateItems = ( myMenuUpdateItems == "") ? "mainDish" : myMenuUpdateItems + ",mainDish";
        }
        if (dessert.isChecked()) {
            myMenuUpdateItems = ( myMenuUpdateItems == "") ? "dessert" : myMenuUpdateItems + ",dessert";
        }

        //to update the menu items
        PartyMenuDB menuDB = new PartyMenuDB(this);
        menuDB.updateMenuItem(eventID, myMenuUpdateItems);

        PartyPlannerDB db = new PartyPlannerDB(this);
        List<String> tmp = db.getEvents().get(Integer.parseInt(eventID));
        tmp.set(PartyPlannerDB.COL_MENU_INDEX, myMenuUpdateItems);
        db.updateEvent(tmp);

        // call the task
        Menu.Task_for_menu mytask= new Menu.Task_for_menu(Menu.this);
        mytask.execute();

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


    /*
     * NAME     :    Task_for_menu
     * PURPOSE :    Menu class contains the functionality of the AsyncTask
     */
    public class Task_for_menu extends AsyncTask<Void, Integer, Void>
    {
        private boolean isCancelled = false;
        Context context;
        Handler handler;
        Dialog dialog;
        TextView txtprogrss;
        ProgressBar progressDialog;
        Button btnCancel;

        Task_for_menu(Context context, Handler handler)
        {
            this.context=context;
            this.handler=handler;
        }

        Task_for_menu(Context context)
        {
            this.context=context;
            this.handler=handler;
        }

        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressBar(Menu.this);

            super.onPreExecute();
            // create dialog
            progressDialog.setMax(10);
            dialog=new Dialog(context);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // dialog.addContentView(progressDialog, InviteActivity.this);
            dialog.setContentView(progressDialog);
            //txtprogrss = (TextView) dialog.findViewById(R.id.txtProgress);
            //  progress=(ProgressBar)dialog.findViewById(progress);

            btnCancel=(Button)dialog.findViewById(R.id.saveBtn);

            dialog.setCancelable(true);

            dialog.show();
        }


        @Override
        protected Void doInBackground(Void... arg0)
        {
            for (int i = 0; i < 100; i++)
            {
                if(i == 10)
                {
                    isCancelled = true;
                    dialog.dismiss();

                    //hide the progress bar
                    progressDialog.setVisibility(ProgressBar.INVISIBLE);

                    break;
                }
                else
                {
                    Log.e("In Background","current value;"+ i);

                    isCancelled = false;

                    try
                    {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e)
                    {
                        isCancelled = true;
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);

            for (int i = 0; i < 10; i++)
            {}
            dialog.dismiss();
            //Toast.makeText(context, "Invitation Sent", Toast.LENGTH_LONG).show();

            // Hide the progress bar
            progressDialog.setVisibility(ProgressBar.INVISIBLE);
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            dialog.dismiss();
            Toast.makeText(context, "Finished", Toast.LENGTH_LONG).show();

            // Hide the progress bar
            progressDialog.setVisibility(ProgressBar.INVISIBLE);
        }
    }
}

