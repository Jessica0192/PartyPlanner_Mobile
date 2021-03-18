package com.example.assignment1;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
 * FILE          : InviteActivity.java
 * PROJECT       : PROG3150 - Assignment #2
 * PROGRAMMER    : Maria Malinina
 * FIRST VERSION : 2020-03-12
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
 * An AsyncTask can be found in this file, which is run in the background when
 * the user is trying to invite the guest. When the user click on the "Send Invita-
 * tion" button, he is presented with "loading" screen of progress bar (also infor-
 * ming him about where the invitation is sent - to which email, if the current guest
 * has an email), and once the invitation is sent, the user is informed that the invi-
 * tation sending process was successful. In case this is not a new event, and the user
 * went to the event details page, stated the event id to update event information and add
 * a new guest, the user can click on the "Update & Save" button to update the guest table
 * in our database and assign a new guest to the guests table.
 */


/*
 * NAME     :   InviteActivity
 * PURPOSE :    This class is used to provide the basic functionality of the guest
 * invitation screen. We have different methods to manipulate the data, display the
 * data that the user might have filled in the event creation screen. We also have
 * different methods for handling different states of the screen. We also keep track
 * of the guest the user sent invitation to and are able to go back to the guest list
 * if the user wants to go back. In addition, there is a functionality that allows the
 * user to hide the keyboard on "Enter" key after he fills out some edit text.
 */
public class InviteActivity extends AppCompatActivity {

    //TAG variable indicating the current activity
    private static final String TAG = "InviteActivity";
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;

    //shared preferences object to store the guests we sent invitations to
    SharedPreferences shared_obj_guests;

    //to keep track of the guest we are currently sending invitation to
    String curGuest;
    //text view object to display the date
    private TextView displayDate;

    //dropdown list object
    private Spinner spinner;
    private String item;
    private String eventID = "";

    //date picker object to make it easier for the user
    // to choose the date
    private DatePickerDialog.OnDateSetListener dateListener;

    //variable where we will store all edit texts we
    //have to be able to hide the keyboard once the user
    //entered all the info
    EditText eventName;
    String evName ="";

    SQLiteDatabase db = null;
    private static String DB_NAME = "PartyPlanner.db";
    public static  String COL_GUEST = "eventGuest";
    private Button btnThread;//btntask;
    // private ProgressDialog progressDialog;

    private String sFileName = "Invitation_Card_For_"; //C:\\MAD\\a2\\Invitation_Card_For_"



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



        //////////////////////////////Passed event id from ViewEventActivity///////////////////////////////////////
        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        Log.d(TAG, "'Invitation' =========event ID===========" + eventID); // test log msg
        ////////////////////////////////////////////////////////////////////////////////////////

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
                    item = parent.getItemAtPosition(position).toString();

                }
            }

            /*
             * FUNCTION   : onNothingSelected()
             * DESCRIPTION: The listener we implemented requires this
             * method to be overridden.
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
        // Button button = findViewById(R.id.sendBtn);



        btnThread=(Button)findViewById(R.id.sendBtn);
        // using a thread to start a process on the button




        //set a listener when we click on item to handle the on click event
        btnThread.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION   : onClick()
             * DESCRIPTION: This function is to inform the user his
             * invitation has been sent to the guest and to save the
             * invited guests.
             * PARAMETERS : View view
             * RETURNS    : NONE
             */
            @Override
            public void onClick(View arg0) {

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

                //instantiate a new intance of the asynchronous task
                Task_for_invitation_activity mytask = new Task_for_invitation_activity(InviteActivity.this);
                //start task execution
                mytask.execute();

                //update the invitation card file name to indicate which guest is getting the invitation card
                // (we create an invitation card once the user clicked on the "send invitation" button, the invitation
                // card is a text file that can be found if we go to the "View" tab -> "Tool Windows" -> "Device File Explorer"
                // go to the "data" folder, then to another "data" folder inside this folder, then to the folder called
                // "com.example.assignment1", then to the folder "files", and there we can find text files "Invitation_Card_For_*guest name*"),
                //as well as the file called "emails.txt", where we retrieve the guests' emails from
                sFileName +=curGuest + ".txt";

                //before we can start generating different files such as invitation cards as text files, we need to ask user to grant us permissions,
                //otherwise android doesn't let you proceed with FILE IO, as it is internal. So if it is the first time the user uses the app, we
                // will get the permissions granted so that we can generate files
                if (ContextCompat.checkSelfPermission(InviteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    // check if the permissions are not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(InviteActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // inform the user about the permissions being requested, this process
                        //is being executed asynchronously
                    } else {
                        //request the permissions
                        ActivityCompat.requestPermissions(InviteActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                }

                //string that will grab the email of the current guest that we want to send
                //invitation to (if the guest has an email available)
                String cur_email = null;

                //file input stream that will enable us to read the file containing the emails
                //and get information from there
                FileInputStream fis = null;
                FileOutputStream fs = null;

                //output stream with help of which we will be able to write data to a file -
                //as we need to produce an invitation card for the user
                FileOutputStream fos = null;


                //first, let's deal with guests' emails
                try{
                    fos = openFileOutput(sFileName, MODE_PRIVATE);
                    fs = openFileOutput("emails.txt", MODE_PRIVATE);
                    fs.write(("maria@gmail.com\n" +
                            "suka@gmail.com\n" +
                            "hoda@gmail.com\n" +
                            "jessica@gmail.com\n" +
                            "troy@gmail.com\n" +
                            "norbert@gmail.com\n" +
                            "igor@gmail.com\n" +
                            "marianna@gmail.com\n" +
                            "yeji@gmail.com\n" +
                            "priyanka@gmail.com").getBytes());
                    try {
                        //open the file containing the guests' emails for reading
                        fis = openFileInput("emails.txt");

                        //instantiate an input stream reader so that we'll be abl to read
                        //the guests' emails from the text file
                        InputStreamReader isr = new InputStreamReader(fis);

                        //implement the buffered reader
                        BufferedReader br = new BufferedReader(isr);

                        //instantiate a sting builder object to grab strings from the file
                        StringBuilder sb = new StringBuilder();

                        //string object where we'll get data from a text file
                        String txt;

                        //read from a text file containing guests emails
                        //line by line
                        while ((txt = br.readLine()) !=null)
                        {
                            //look for the selected guest's email in the email list
                            if (txt.contains(curGuest.toLowerCase()))
                            {
                                //save the guest's email
                                cur_email = txt;
                                System.out.println("Current guest email found");
                            }

                            //append a new line so that we'll be able to go through
                            //the file's data one line at a time
                            sb.append(txt).append("\n");
                        }

                        //check just in case if there is no email for this guest in the list
                        if (cur_email == null)
                        {
                            //inform
                            cur_email = "No email available";
                        }

                    //catch all the exceptions
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //put all the necessary information in the invitation card file
                    String invitation = "\tInvitation to: "+curGuest;
                    String ev_name = "\tEvent Name: " + evName;
                    fos.write(invitation.getBytes());

                    //we use getBytes() to appropriately append information to the file
                    fos.write("\n".getBytes());
                    fos.write(("\tGuest Email: " + cur_email).getBytes());
                    fos.write("\n".getBytes());
                    fos.write(ev_name.getBytes());
                    fos.write("\n".getBytes());
                    EditText description  = findViewById(R.id.descrEditText);

                    String str_descr;

                    //if there is no event description available, inform the user
                    if (description.getText().toString() == null)
                    {
                        str_descr = "*no description or message available*";
                    }
                    else
                    {
                        str_descr = description.getText().toString();
                    }

                    //add other details
                    String full_descr = "\tEvent Description: " + str_descr;
                    fos.write(full_descr.getBytes());
                    fos.write("\n".getBytes());
                    TextView sel_date  = findViewById(R.id.selectDate);
                    String ev_date ="\tEvent Date: " + sel_date.getText().toString();
                    fos.write(ev_date.getBytes());
                    fos.write("\n".getBytes());
                    EditText addr = findViewById(R.id.addr);
                    EditText city = findViewById(R.id.city);
                    String ev_addr = "\tEvent Address: " + addr.getText().toString() + ", "+ city.getText().toString() + ", " + item;
                    fos.write(ev_addr.getBytes());
                    fos.write("\n".getBytes());

                    //print out the information
                    System.out.println("File being processed, trying to save to " + getFilesDir() + "/" + sFileName);

                    //catch all the exceptions
                }catch (FileNotFoundException e){
                    e.printStackTrace();

                }catch (IOException e){

                    e.printStackTrace();
                } finally{
                    if (fos != null)
                    {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //inform the user what email we are sending the invitation to
                Toast.makeText(InviteActivity.this, "Sending Invitation to " + cur_email, Toast.LENGTH_LONG).show();

            }
        });


        //hide the keyboard for each of the edit text when
        //the user puts some text in them, because after
        //he is done, the keyboard needs to hide (so that
        //we have a user friendly app)

        eventName = findViewById(R.id.inviteEditText);
        handleKeyBHiding();

        eventName = findViewById(R.id.descrEditText);
        handleKeyBHiding();

        eventName = findViewById(R.id.city);
        handleKeyBHiding();

        eventName = findViewById(R.id.addr);
        handleKeyBHiding();



        //get the shared preferences object called "Saved" that filled in in the GuestActivity
        //while selecting who to possibly send invitation to
        SharedPreferences sp2 = getApplicationContext().getSharedPreferences("Saved Values", MODE_PRIVATE);

        //get the event name value that we filled in in the event creation screen
        evName = sp2.getString("eventName","");

        //find the edit text that is used for the event name by id
        EditText evNameEdit = findViewById(R.id.inviteEditText);


        // show the event name in the edit text, filled out for the user
        evNameEdit.setText(evName);

        //get the date string from the shared preferences object
        String date = sp2.getString("date", "");

        //get the text view responsible for the date display by its id
        TextView dateEditText = findViewById(R.id.selectDate);

        //set the date to the edit text that displays the date
        dateEditText.setText(date);

        //get the edit text for the address field
        EditText addrEdit = findViewById(R.id.addr);

        //get the address string from the shared preferences object
        String address = sp2.getString("address", "");

        //display the address that the user typed in the
        //event creation screen
        addrEdit.setText(address);



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
        finish();
    }

    /*
     * FUNCTION   : updDb()
     * DESCRIPTION: This function is called whenever the user wants to
     * update guest information about existing event. We instantiate a new
     * database and insert a new guest to the guest table.
     * PARAMETERS : View view
     * RETURNS    : NONE
     */
    public void updDb(View view){
        //to update the guest items
        PartyGuestDB inviteDB = new PartyGuestDB(this);
        inviteDB.updateGuest(eventID, curGuest);

        PartyPlannerDB db = new PartyPlannerDB(this);
        List<String> tmp = db.getEvents().get(Integer.parseInt(eventID));
        tmp.set(PartyPlannerDB.COL_MENU_INDEX, curGuest);
        db.updateEvent(tmp);
        Toast.makeText(InviteActivity.this, "Updated Event Information", Toast.LENGTH_SHORT).show();

    }

    /*
     * FUNCTION   : hideKeyboard
     * DESCRIPTION: This function is called to hide the keyboard
     * PARAMETERS : EditText et: EditText widget is passed
     * RETURNS    :NONE
     */
    private void hideKeyboard(EditText et)
    {
        InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }



    /*
     * FUNCTION   : handleKeyBHiding()
     * DESCRIPTION: This function handles keyboard hiding
     * on click of "Enter" button.
     * PARAMETERS : NONE
     * RETURNS    : NONE
     */
    private void handleKeyBHiding(){

        //whatever editText we are working with right now
        // (which is eventName variable), we set a listener
        // on it
        eventName.setOnKeyListener(new View.OnKeyListener() {
            /*
             * FUNCTION   : onKey()
             * DESCRIPTION: This function returns if we need to
             * hide keyboard or not.
             * PARAMETERS : View v,
             *              int keyCode,
             *              KeyEvent keyEvent
             * RETURNS    : true, if there is some action
             *              requiring to hide the keyboard
             *              false, if not
             */
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
                //if there is an action requiring to hide the keyboard
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    hideKeyboard((EditText) eventName);
                    return true;
                }
                return false;
            }
        });

    }









    /*
     * NAME     :   Task_for_invitation_activity
     * PURPOSE :    Task_for_invitation_activity class contains the functionality of the AsyncTask to have a progress bar
     *              AsyncTask  is for helper class around Thread and Handler
     */
    public class Task_for_invitation_activity extends AsyncTask<Void, Integer, Void> {

        //a boolean variable indicating if the task is cancelled or no
        private boolean isCancelled = false;
        //the context of the task
        Context context;

        //task handler
        Handler handler;

        //dialog that we'll display
        Dialog dialog;

        //progress dialog we'll display when the sending invitation process
        //is in progress
        ProgressBar progressDialog;
       // Button btnCancel;

        Task_for_invitation_activity(Context context, Handler handler){
            this.context=context;
            this.handler=handler;

        }

        Task_for_invitation_activity(Context context){
            this.context=context;
            this.handler=handler;
        }

        @Override
        protected void onPreExecute() {

            //progress dialog
//just commented this out
            progressDialog = new ProgressBar(InviteActivity.this);

            super.onPreExecute();
            // create dialog
            progressDialog.setMax(10);
            dialog=new Dialog(context);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // dialog.addContentView(progressDialog, InviteActivity.this);
            dialog.setContentView(progressDialog);


           // btnCancel=(Button)dialog.findViewById(R.id.sendBtn);

            dialog.setCancelable(true);


            dialog.show();


        }


        @Override
        protected Void doInBackground(Void... arg0) { //Void... arg0)


            // while(true){
            // int i = 0;
            for ( int i = 0; i < 100; i++) {
                // if(isCancelled()){
                if (i == 10){
                    isCancelled = true;
                    dialog.dismiss();
//                    Toast.makeText(context, "Finished", Toast.LENGTH_LONG).show();


                    // Hide the progress bar
                    progressDialog.setVisibility(ProgressBar.INVISIBLE);
                    //  i = 0;
                    //Task_for_invitation_activity.this.
                    break;
                }else{
                    Log.e("In Background","current value;" + i); // + i
                    // publishProgress(i);
                    isCancelled = false;

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        //dont forget the catch block
                        isCancelled = true;
                        e.printStackTrace();
                    }

                }
            }

            return null;
            // return isCancelled;
        }



        @Override
        protected void onProgressUpdate(Integer... values) {


            super.onProgressUpdate(values);


            for (int i = 0; i < 10; i++)
            {}
            dialog.dismiss();
            //Toast.makeText(context, "Invitation Sent", Toast.LENGTH_LONG).show();


            // Hide the progress bar
            progressDialog.setVisibility(ProgressBar.INVISIBLE);
            //          progressDialog.setProgress(values[0]);
//            txtprogrss.setText("progress update"+ values[0]+"%");

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            dialog.dismiss();
            Toast.makeText(context, "Invitation Sent", Toast.LENGTH_LONG).show();


            // Hide the progress bar
            progressDialog.setVisibility(ProgressBar.INVISIBLE);

        }





    }
}

