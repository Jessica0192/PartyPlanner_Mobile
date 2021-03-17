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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
 * FILE          : InviteActivity.java
 * PROJECT       : PROG3150 - Assignment #1
 * PROGRAMMER    : Maria Malinina
 * FIRST VERSION : 2020-02-10
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

    //date picker object to make it easier for the user
    // to choose the date
    private DatePickerDialog.OnDateSetListener dateListener;

    //variable where we will store all edit texts we
    //have to be able to hide the keyboard once the user
    //entered all the info
    EditText eventName;

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
        // btntask=(Button)findViewById(R.id.btntsk);

        //  btntask.setOnClickListener(new OnClickListener() {
//
        //          @Override
        //        public void onClick(View arg0) {
///

        //start myTast

        //             new MyTask(MainActivity.this).execute();


        //       }
        //  });


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


                //progress
//                //progress dialog
//                progressDialog = new ProgressDialog(InviteActivity.this);
//                progressDialog.setMessage("Sending Invitation..."); // Setting Message
//                progressDialog.setTitle("ProgressDialog"); // Setting Title
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//                progressDialog.show(); // Display Progress Dialog
//                progressDialog.setCancelable(false);
//                new Thread(new Runnable() {
//                    public void run() {
//                        try {
//                            Thread.sleep(10000);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        progressDialog.dismiss();
//                    }
//                }).start();


                //new

                //  db =  SQLiteDatabase.openDatabase(DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);

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


//                try {
//
//                    //   Toast.makeText(this, "table created ", Toast.LENGTH_LONG).show();
//                  //  String sql =
//                    //        "INSERT or replace INTO "+ COL_GUEST +" (INVITATION_STATUS_CODE) VALUES(1) WHERE NAME=''" ;
//                    db.execSQL("UPDATE "+COL_GUEST+" SET INVITATION_STATUS_CODE = 1"+ "WHERE NAME = "+ curGuest);
//                   // db.execSQL(sql);
//                }
//                catch (Exception e) {
//                    //     Toast.makeText(this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
//                }

                //task here
                //       new Task_for_invitation_activity().execute();
                Task_for_invitation_activity mytask = new Task_for_invitation_activity(InviteActivity.this);

                mytask.execute();
                // mytask.wait(10);
                 sFileName +=curGuest + ".txt";
                if (ContextCompat.checkSelfPermission(InviteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(InviteActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(InviteActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    }
                }

                FileOutputStream fos = null;
                try{
                    fos = openFileOutput(sFileName, MODE_PRIVATE);
                    String invitation = "\tInvitation to: "+curGuest;
                    String ev_name = "\tEvent Name: " + eventName;
                    fos.write(invitation.getBytes());

                   fos.write("\n".getBytes());
                    fos.write(ev_name.getBytes());
                    fos.write("\n".getBytes());
                    EditText description  = findViewById(R.id.descrEditText);

                    String str_descr;
                    if (description.getText().toString() == null)
                    {
                        str_descr = "*no description or message available*";
                    }
                    else
                    {
                        str_descr = description.getText().toString();
                    }

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
                    System.out.println("File being processed, trying to save to " + getFilesDir() + "/" + sFileName);
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
                //BEGINNING OF THE FILE NAME

//                File file = new File("C:\\MAD\\music.txt"); //initialize File object and passing path as argument
//                boolean result;
//                try
//                {
//                    result = file.createNewFile();  //creates a new file
//                    if(result)      // test if successfully created a new file
//                    {
//                        System.out.println("file created "+file.getCanonicalPath()); //returns the path string
//                    }
//                    else
//                    {
//                        System.out.println("File already exist at location: "+file.getCanonicalPath());
//                    }
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();    //prints exception if any
//                    System.out.println("Error in file creation");
//                }
//
//                try {
//                    FileOutputStream fileout=openFileOutput(sFileName, MODE_PRIVATE);
//                    OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
//                    outputWriter.write("HELLO");
//                    outputWriter.close();
//                    //display file saved message
//                    Toast.makeText(getBaseContext(), "File saved successfully!",
//                            Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//                try {
//                    File root = new File(Environment.getExternalStorageDirectory(), "Notes");
//                    if (!root.exists()) {
//                        root.mkdirs();
//                    }
//                    File gpxfile = new File(root, sFileName);
//                    FileWriter writer = new FileWriter(gpxfile);
//
//                    writer.append("\tInvitation to: "+curGuest);
//                    writer.append("\n");
//                    writer.append("\tEvent Name: " + eventName);
//                    writer.append("\n");
//                    EditText description  = findViewById(R.id.descrEditText);
//
//                    String str_descr;
//                    if (description.getText().toString() == null)
//                    {
//                        str_descr = "*no description or message available*";
//                    }
//                    else
//                    {
//                        str_descr = description.getText().toString();
//                    }
//
//                    writer.append("\tEvent Description: " + str_descr);
//                    writer.append("\n");
//                    TextView sel_date  = findViewById(R.id.selectDate);
//                    writer.append("\tEvent Date: " + sel_date.getText().toString());
//                    writer.append("\n");
//                    EditText addr = findViewById(R.id.addr);
//                    EditText city = findViewById(R.id.city);
//                    writer.append("\tEvent Address: " + addr.getText().toString() + ", "+ city.getText().toString() + ", " + item);
//                    writer.append("\n");
//                    writer.flush();
//                    writer.close();
//                    Toast.makeText(InviteActivity.this, "Saved", Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                ////END OF FILE THING


//                try {
//                    Thread.sleep(5 * 1000);
//                } catch (InterruptedException ie) {
//                    Thread.currentThread().interrupt();
//                }
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Do something after 5s = 5000ms
//                    }
//                }, 5000);

                //          mytask.cancel(true);
                // mytask.onPostExecute();


                //inform the user that the invitation has been sent
                //Toast.makeText(InviteActivity.this, "Invitation sent", Toast.LENGTH_SHORT).show();

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
        String evName = sp2.getString("eventName","");

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










    // new class

    public class Task_for_invitation_activity extends AsyncTask<Void, Integer, Void> {

        private boolean isCancelled = false;
        Context context;
        Handler handler;
        Dialog dialog;
        TextView txtprogrss;
        ProgressBar progressDialog;
        Button btnCancel;

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
            txtprogrss = (TextView) dialog.findViewById(R.id.txtProgress);
            //  progress=(ProgressBar)dialog.findViewById(progress);

            btnCancel=(Button)dialog.findViewById(R.id.sendBtn);
//
//            btnCancel.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//
//
//                    Task_for_invitation_activity.this.cancel(true);
//                }
//            });

            //where to show the dialog???


            //dialog.setIndeterminate(true);
            dialog.setCancelable(true);
//            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    Task_for_invitation_activity.this.cancel(true);
//                    Toast.makeText(InviteActivity.this,"AsyncTask is stopped",Toast.LENGTH_LONG).show();
//                    dialog.dismiss();
//                    Toast.makeText(context, "Finished", Toast.LENGTH_LONG).show();
//
//
//                    // Hide the progress bar
//                    progressDialog.setVisibility(ProgressBar.INVISIBLE);
//                }
//            });

            dialog.show();

//            try {
//                Thread.sleep(5 * 1000);
//            } catch (InterruptedException ie) {
//                Thread.currentThread().interrupt();
//            }
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    // Do something after 5s = 5000ms
//                }
//            }, 5000);
            //         dialog.dismiss();
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
            Toast.makeText(context, "Invitation Sent", Toast.LENGTH_LONG).show();


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

