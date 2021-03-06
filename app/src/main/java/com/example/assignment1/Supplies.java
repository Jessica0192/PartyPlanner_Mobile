/*
 * FILE          : Supplies.java
 * PROJECT       : PROG3150 - Assignment #2
 * PROGRAMMER    : Hoda Akrami
 * FIRST VERSION : 2020-02-10
 * DESCRIPTION   : This file contains the functionality behind the supply.xml file.
 *                 When the user is presented with this screen, he had a list view of supplies,
 *                 he can select the item that he wants to use in his party and save it.
 *                 Also, he can save the update if he wish and back to event details page.
 */

package com.example.assignment1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
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

import java.util.List;

/*
 * NAME     :    Supplies
 * PURPOSE :    Supplies class contains the functionality behind the supply.xml file.
 *              It contains functions for loading the page, saving the selected items on the shared preferences,
 *              and getting back to the activity page.
 */
public class Supplies extends AppCompatActivity{

    private TextView supplies = null;
    private CheckBox balloons = null;
    private CheckBox cake = null;
    private CheckBox flowers = null;
    private CheckBox cups = null;
    private CheckBox cutlery = null;
    private CheckBox candle = null;
    private CheckBox invitations = null;
    private Button save = null;
    private SharedPreferences savedValues = null;
    private String eventID = "";
    private String mySuppliesUpdateItems = "";
    SharedPreferences supplyStorage;
    public static final String TAG = "supplies";

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
        setContentView(R.layout.supply);

        balloons = findViewById(R.id.checkbox_balloons);
        cake = findViewById(R.id.checkbox_cake);
        flowers = findViewById(R.id.checkbox_flowers);
        cups = findViewById(R.id.checkbox_cups);
        cutlery = findViewById(R.id.checkbox_cutlery);
        candle = findViewById(R.id.checkbox_candle);
        invitations = findViewById(R.id.checkbox_invitations);

        //Passed event id from ViewEventActivity
        Intent updateSupplyIntent = getIntent();
        eventID = updateSupplyIntent.getStringExtra("eventID");
        Log.d(TAG, "'Supply' =========event ID===========" + eventID); // test log msg

        supplyStorage = getSharedPreferences("SupplySelected", Context.MODE_PRIVATE);
        supplyStorage.edit().clear().apply();

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
                if (balloons.isChecked())
                {
                    result.append("balloons");
                }
                if (cake.isChecked())
                {
                    result.append("/");
                    result.append("cake");
                }
                if (flowers.isChecked())
                {
                    result.append("/");
                    result.append("flowers");
                }
                if (cups.isChecked())
                {
                    result.append("/");
                    result.append("cups");
                }
                if (cutlery.isChecked())
                {
                    result.append("/");
                    result.append("cutlery");
                }
                if (candle.isChecked())
                {
                    result.append("/");
                    result.append("candle");
                }
                if (invitations.isChecked())
                {
                    result.append("/");
                    result.append("invitations");
                }

                //to save the selected items on shared preferences
                SharedPreferences.Editor editor = supplyStorage.edit();

                editor.putString("SupplyItems", result.toString());
                editor.apply();

                // call the task
                Supplies.Task_for_supplies mytask= new Supplies.Task_for_supplies(Supplies.this);
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
        String link = "https://www.amazon.ca/s?k=party+suplies&ref=nb_sb_noss_2";
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
        if (balloons.isChecked()) {

            mySuppliesUpdateItems = ( mySuppliesUpdateItems == "") ? "balloons" : mySuppliesUpdateItems + ",balloons";
        }
        if (cake.isChecked()) {

            mySuppliesUpdateItems = ( mySuppliesUpdateItems == "") ? "cake" : mySuppliesUpdateItems + ",cake";
        }
        if (flowers.isChecked()) {

            mySuppliesUpdateItems = ( mySuppliesUpdateItems == "") ? "flowers" : mySuppliesUpdateItems + ",flowers";
        }
        if (cups.isChecked()) {

            mySuppliesUpdateItems = ( mySuppliesUpdateItems == "") ? "cups" : mySuppliesUpdateItems + ",cups";
        }
        if (cutlery.isChecked()) {

            mySuppliesUpdateItems = ( mySuppliesUpdateItems == "") ? "cutlery" : mySuppliesUpdateItems + ",cutlery";
        }
        if (candle.isChecked()) {

            mySuppliesUpdateItems = ( mySuppliesUpdateItems == "") ? "candle" : mySuppliesUpdateItems + ",candle";
        }
        if (invitations.isChecked()) {

            mySuppliesUpdateItems = ( mySuppliesUpdateItems == "") ? "invitations" : mySuppliesUpdateItems + ",invitations";
        }
        //to update the supplies items
        PartySupplyDB supplyDB = new PartySupplyDB(this);
        supplyDB.updateSupply(eventID, mySuppliesUpdateItems);

        PartyPlannerDB db = new PartyPlannerDB(this);
        List<String> tmp = db.getEvents().get(Integer.parseInt(eventID));
        tmp.set(PartyPlannerDB.COL_SUPPLY_INDEX, mySuppliesUpdateItems);
        db.updateEvent(tmp);

        // call the task
        Supplies.Task_for_supplies mytask= new Supplies.Task_for_supplies(Supplies.this);
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
    public void onPause()
    {
        Log.d(TAG, "'Supplies' Page Paused");
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
        Log.d(TAG, "'Supplies' Page Resumed");
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
        Log.d(TAG, "'Supplies' Page Stopped");
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
        Log.d(TAG, "'Supplies' Page Destroyed");
        super.onDestroy();
    }


    /*
     * NAME     :    Task_for_supplies
     * PURPOSE :    Supplies class contains the functionality of the AsyncTask class contains the functionality of the AsyncTask to have a progress bar
     *              AsyncTask  is for helper class around Thread and Handler
     */
    public class Task_for_supplies extends AsyncTask<Void, Integer, Void>
    {
        private boolean isCancelled = false;
        Context context;
        Handler handler;
        Dialog dialog;
        TextView txtprogrss;
        ProgressBar progressDialog;
        Button btnCancel;

        Task_for_supplies(Context context, Handler handler)
        {
            this.context=context;
            this.handler=handler;
        }

        Task_for_supplies(Context context)
        {
            this.context=context;
            this.handler=handler;
        }


        /*  -- Function Header Comment
        Name	:   onPreExecute()
        Purpose :   This function is called to provides a way of decoupling task submission from the mechanics of how task runs
        Inputs	:	NONE
        Outputs	:	NONE
        Returns	:	NONE
        */
        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressBar(Supplies.this);

            super.onPreExecute();
            // create dialog
            progressDialog.setMax(10);
            dialog=new Dialog(context);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(progressDialog);

            btnCancel=(Button)dialog.findViewById(R.id.saveBtn);
            dialog.setCancelable(true);
            dialog.show();
        }


        /*  -- Function Header Comment
        Name	:   doInBackground(Void... arg0)
        Purpose :   This function is called right after onPreExecute finishes and
                    uses the background thread. This is where you’ll doing your work.
        Inputs	:	Void... arg0
        Outputs	:	NONE
        Returns	:	NONE
        */
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


        /*  -- Function Header Comment
        Name	:   onProgressUpdate(Integer... values)
        Purpose :   is called on the UI thread and is used to display progress to the user while the task is running
        Inputs	:	Integer... values
        Outputs	:	NONE
        Returns	:	NONE
        */
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);

            for (int i = 0; i < 10; i++)
            {}
            dialog.dismiss();

            // Hide the progress bar
            progressDialog.setVisibility(ProgressBar.INVISIBLE);
        }


        /*  -- Function Header Comment
       Name	:   onPostExecute(Void result)
       Purpose :   is called right after ‘doInBackground’ finishes and uses the UI thread.
       Inputs	:	Void result
       Outputs	:	NONE
       Returns	:	NONE
       */
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            dialog.dismiss();
            //Displaying the message on the toast
            Toast.makeText(context, "Supply items selected has been saved!", Toast.LENGTH_LONG).show();

            // Hide the progress bar
            progressDialog.setVisibility(ProgressBar.INVISIBLE);
        }
    }
}
