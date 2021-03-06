package com.example.assignment1;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
/*
 * FILE          : GuestActivity.java
 * PROJECT       : PROG3150 - Assignment #2
 * PROGRAMMER    : Maria Malinina
 * FIRST VERSION : 2020-03-12
 * DESCRIPTION   :
 * This file contains the functionality behind the choose_guests.xml screen.
 * When the user is presented with this screen, he had a list view of guests
 * he can invite, he can scroll through them, or start typing name/letter in
 * the search box and find the guest he wants, then he can press invite in order
 * to invite him. So here we have an onCreate function which just defines the
 * basic functionality of the screen, and on button click functions, which are
 * used to 1. Redirect us to the content_invite.xml screen if the user chose to
 * invite some guest, 2. Redirect us back to the create event page, if the user
 * wants to go back. Before going to invitation screen, we store the selected
 * guest in a shared preference variable to make sure in the invitation screen
 * we know who we are sending invitation to.
 * We also access the eventID passed from the ViewEventActivity to be able to
 * update the database' guest table later in case we have any updates for that.
 * We also use adapters here.
 */


/*
 * NAME     :   GuestActivity
 * PURPOSE :    This class is used to provide the basic functionality of the guest
 * selection screen. Here, we have an array of all of the possible guests that the
 * user may choose to invite, so we fill out the list view programmatically to con-
 * tain all of the possible guests. We also manipulate data, we keep track of the
 * guest the user selected to send invitation to in a shared preference object to
 * be able to retrieve it in the guest invitation screen. We also implement a search
 * view to display the contents of the list view according to what the user typed in
 * the search (if he typed something). And we highlight the selected guest. The buttons
 * allow the user to either proceed with guest invitation or go back to the event cre-
 * ation screen. The keyboard is automatically hidden if the user types something in the
 * search view and then presses the "enter" button.
 */
public class GuestActivity extends AppCompatActivity {

    // to keep track of the guests
    ArrayList<String> selectedGuests = new ArrayList<>();
    //shared preferences object
    SharedPreferences sp_obj;
    //our list view
    ListView guestList;
    //button
    private Button button;
    // searchview object
    SearchView searchView;

    //an array containing the names of guests to choose from
    String[] nameList = {"Maria", "Suka", "Hoda", "Jessica", "Troy", "Norbert", "Igor", "Marianna", "Yeji", "Priyanka", ""};

    //adapter for the array
    ArrayAdapter<String> arrayAdapter;

    private EditText mNewGuestNameEditText;
    private EditText mNewPartySizeEditText;

    //LOG_TAG that we'll use for logging
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    private String eventID = "";

    //NEW THING
    SQLiteOpenHelper dbHelper = null;
    SQLiteDatabase db = null;

    // DatabaseHelper dbhelper = null;
    int id_counter = 0;
    //private static String DB_PATH = "/data/data/YOUR_PACKAGE/databases/";

    private static String DB_NAME = "PartyPlanner.db";
    public static  String COL_GUEST = "eventGuest";
    //END OF NEW THING

    /*
     * FUNCTION   : onCreate()
     * DESCRIPTION: This function is called when the page is loaded, so here
     * we have some basic functionality of the page where the user can select
     * which guests to invite to a party. Here, we simply add all the possible
     * guests to the listView to display them and make the options selectable.
     * PARAMETERS : Bundle savedInstanceState - save instance state
     * RETURNS    : NONE
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        //set the content of what we see
        setContentView(R.layout.choose_guests);

        //////////////////////////////Passed event id from ViewEventActivity///////////////////////////////////////
        Intent updateGuestIntent = getIntent();
        eventID = updateGuestIntent.getStringExtra("eventID");
        ////////////////////////////////////////////////////////////////////////////////////////

        //find the list view in our xml by id
        guestList=findViewById(R.id.guestList);

        //find the search view from xml by id
        searchView = findViewById(R.id.search_bar);

        //set a new array adapter to put the list of possible guests into it and adapt
        //it for the list view
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, android.R.id.text1, nameList);

        //make the list view display the array of possible guests
        guestList.setAdapter(arrayAdapter);


        //set a listener when we click on item to handle the on click event
        guestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /*
             * FUNCTION   : onItemClick()
             * DESCRIPTION: If some guest from the listview is clicked, we
             * simply add him to our array list of guests.
             * PARAMETERS : AdapterView<?> adapterView,
             *              View view,
             *              int i,
             *              long l
             * RETURNS    : NONE
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //add selected guest to the list of guests
                selectedGuests.add(guestList.getItemAtPosition(i).toString());




            }
        });


        //set on query listener to properly handle our search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            /*
             * FUNCTION   : onQueryTextSubmit()
             * DESCRIPTION: When we provide text to search view,
             * the string needs to be found in list of guests.
             * PARAMETERS : String query
             * RETURNS    : false
             */
            @Override
            public boolean onQueryTextSubmit(String query){
                GuestActivity.this.arrayAdapter.getFilter().filter(query);
                return false;
            }


            /*
             * FUNCTION   : onQueryTextChange()
             * DESCRIPTION: Handle the search of a guest/guests.
             * PARAMETERS : String newText
             * RETURNS    : false
             */
            @Override
            public boolean onQueryTextChange(String newText){
                GuestActivity.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });




    }



    /*
     * FUNCTION   : viewEvDets()
     * DESCRIPTION: This function simply redirects us to to the
     * invitation screen - content_invite.xml, which corres-
     * ponds with ViewEventActivity.
     * PARAMETERS : View view
     * RETURNS    : NONE
     */
    public void viewEvDets(View view){
        //go to the invitation screen
        Intent intent = new Intent(this, ViewEventActivity.class);
        startActivity(intent);
    }
    /*
     * FUNCTION   : goToInviteActivity()
     * DESCRIPTION: This function simply redirects us to to the
     * invitation screen - content_invite.xml, which corres-
     * ponds with InviteActivity.
     * PARAMETERS : NONE
     * RETURNS    : NONE
     */
    public void goToInviteActivity(){
        //go to the invitation screen
        Intent intent = new Intent(this, InviteActivity.class);
        intent.putExtra("eventID", eventID);
        startActivity(intent);
    }


    /*
     * FUNCTION   : backToEventCreation()
     * DESCRIPTION: This function is a simple on click event,
     * if the user clicks on "back to event creation" - we go to the
     * event creation screen, which corresponds with CreateEventActivity.
     * PARAMETERS : View view
     * RETURNS    : NONE
     */
    public void backToEventCreation (View view)
    {
        //go back to event creation screen
        finish();
    }



    /*
     * FUNCTION   : addToWaitList()
     * DESCRIPTION: This function is a simple on click event,
     * if the user clicks on "invite" - we save the guest the user
     * selected, to know who to send invitation to, and go to the
     * invitation screen (content_invite.xml), which corresponds with
     * InviteActivity.
     * PARAMETERS : View view
     * RETURNS    : NONE
     */
    public void addToWaitList(View view)
    {
        //get the list view of guests from xml
        guestList = (ListView)findViewById(R.id.guestList);

        //establish a new array list object
        ArrayList<String> arrayList = new ArrayList<>();


        //find our search view bar by id
        searchView = findViewById(R.id.search_bar);

        //set up a new adapter that is going to adapt the array to our list view
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, android.R.id.text1,nameList);

        //adapt
        guestList.setAdapter(arrayAdapter);


        //establish a new shared preferences object named "GuestPrefs"
        sp_obj = getSharedPreferences("GuestPrefs", Context.MODE_PRIVATE);

        //editor object to fill the "GuestPrefs" Shared Preferences
        SharedPreferences.Editor editor = sp_obj.edit();

        //get the size of the array where we added the selected guest
        int arraySize = selectedGuests.size();
        if((arraySize - 1) != -1) {
            //save the current guest into the shared preferences object, as
            //we are going need to know which guest we are sending invitation
            // to in out InviteActivity
            editor.putString("cur_guest", selectedGuests.get(arraySize - 1));

            //apply saved shared preferences
            editor.apply();
            //redirecting to the content_invite.xml screen for invitation
            goToInviteActivity();
        }
        else
        {
            //if a user didn't select any guest and pressed "invite"
            //button - display an error message
            Toast.makeText(getApplicationContext(), "Please select one of the guests to invite", Toast.LENGTH_SHORT).show();
        }


    }

    /*
     * FUNCTION   : onResume
     * DESCRIPTION:
     *      This function is called the current page is in the state of Resume
     * PARAMETER  : NONE
     * RETURNS    : NONE
     */
    @Override
    public void onResume()
    {
        //log in the state of the page
        Log.d(LOG_TAG, "'Guest Activity' Page Resumed");
        super.onResume();
    }



    /*
     * FUNCTION   : onPause
     * DESCRIPTION:
     *      This function is called the current page is in the state of Pause
     * PARAMETER  : NONE
     * RETURNS    : NONE
     */
    @Override
    public void onPause()
    {
        //log in the state of the page
        Log.d(LOG_TAG, "'Guest Activity' Page Paused");
        super.onPause();
    }


    /*
     * FUNCTION   : onStop
     * DESCRIPTION:
     *      This function is called the current page is in the state of Stop
     * PARAMETER  : NONE
     * RETURNS    : NONE
     */
    @Override
    public void onStop()
    {
        //log in the state of the page
        Log.d(LOG_TAG, "'Guest Activity' Page Stopped");
        super.onStop();
    }

    /*
     * FUNCTION   : onDestroy
     * DESCRIPTION:
     *      This function is called the current page is in the state of Destroy
     * PARAMETER  : NONE
     * RETURNS    : NONE
     */
    @Override
    public void onDestroy()
    {
        //log in the state of the page
        Log.d(LOG_TAG, "'Guest Activity' Page Destroyed");
        super.onDestroy();
    }

}