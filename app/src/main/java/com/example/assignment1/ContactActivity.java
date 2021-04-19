/*
 *   FILE: ContactActivity.java
 *   Project: A3
 *   PROGRAMMER: Jessica Sim
 *   FIRST VERSION: 2021-04-01
 *   DESCRIPTION:
 *	    This file is using content provider and get the Contact information. Since there were so many things happening
 *      in main thread, I had to use sub-thread to spread the contact information that I got from Contacts application
 *      to spread it into a UI
 */
package com.example.assignment1;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
 * NAME     :    ContactActivity
 * PURPOSE :    ContactActivity class contains the functionality behind the activity_contact_info.xml file.
 *              It contains functions for getting contact from the Contacts application and
 *              spread information in the UI
 */
public class ContactActivity extends Activity  {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS=1;
    public static final String TAG = "ContactActivity";
    public TextView outputText;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_contact_info);

        outputText =(TextView)

        findViewById(R.id.output);

        class NewRunnable implements Runnable {
            @Override
            /*
             * FUNCTION: run
             * DESCRIPTION:
             *      This function is to run in a sub-thread
             * PARAMETER:
             *      there's no parameters
             * RETURNS:
             *      void: there's no return value
             */
            public void run() {

                runOnUiThread(new Runnable() { public void run() {
                    if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M
                            &&
                            checkSelfPermission(Manifest.permission.READ_CONTACTS) !=PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    } else
                    {
                        fetchContacts();
                    }
                }
                });
            }
        }

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();

        Button backBtn = findViewById(R.id.contactBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when BACK button is clicked
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
         * FUNCTION: fetchContacts
         * DESCRIPTION:
         *      This function is called to fetch the contact information to TextView in UI
         * PARAMETER:
         *      there's no parameters
         * RETURNS:
         *      void: there's no return value
         */
        //@TargetApi(Build.VERSION_CODES.ECLAIR)
        public void fetchContacts() {
            String phoneNumber = null;
            String email = null;
            Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
            String _ID = ContactsContract.Contacts._ID;
            String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
            Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
            String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
            Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
            String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
            String DATA = ContactsContract.CommonDataKinds.Email.DATA;
            StringBuffer output = new StringBuffer();
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
            // Loop for every contact in the phone
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                    String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                    if (hasPhoneNumber > 0) {
                        output.append("\n Name:" + name);
                        // Query and loop for every phone number of the contact
                        Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                        while (phoneCursor.moveToNext()) {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                            output.append("\n Phone number:" + phoneNumber);
                        }
                        phoneCursor.close();
                        // Query and loop for every email of the contact
                        Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);
                        while (emailCursor.moveToNext()) {
                            email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                            output.append("\nEmail:" + email);
                        }
                        emailCursor.close();
                    }
                    output.append("\n");
                }
                outputText.setText(output);
            }
        }


    /*
     * FUNCTION: onRequestPermissionsResult
     * DESCRIPTION:
     *      This function is called to request the permission result
     * PARAMETER:
     *      int requestCode: integer value of request code
     *      String[] permissions: string array which contains permissions
     *      int[] grantResults: int array which contains the result of grant
     * RETURNS:
     *      void: there's no return value
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchContacts();
                } else {
                    // permission denied! Disable the
                    // functionality that depends on this permission.
                    Log.d(TAG, "permission denied");
                }
                return;
            }
        }
    }

}


