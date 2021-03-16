/*
 * FILE          : MainActivity.java
 * PROJECT       : PROG3150 - Assignment #2
 * PROGRAMMER    : Hoda Akrami, Maria Malinina, Jessica Sim, Suka Sun
 * FIRST VERSION : 2020-03-19
 * DESCRIPTION   : This file contains the start page of the Perfect Party app.
 *                 Perfect Party app: This app is used to plan parties. The user can review
 *                 existing events, add event, and delete all the existing events. When creating
 *                 a new event, the user may set the party's name, select the purpose of the party,
 *                 set date of the party, set the address where the party will be held, add guests,
 *                 and send invitation to guests. The user can also add food and party supplies.
 *                 Once a new event is created, the name and date of the party will be added to the
 *                 Events page.
 */

package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.KeyEvent;


/*
 * NAME     :   MainActivity
 * PURPOSE :    MainActivity class contains the functionality behind the activity_main.xml file.
 *              It contains the app start page and leads to the EventListActivity page.
 */
public class MainActivity extends AppCompatActivity {

    private static final String USER_NAME = "herd";
    private static final String PASS_WORD =  "set";
    private EditText username = null;
    private EditText password = null;
    Button btnLogin = null;
    public static final String TAG = "EventListActivity";

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText)findViewById(R.id.txtUsername);
        password = (EditText)findViewById(R.id.txtPassword);

        username.setOnKeyListener(new View.OnKeyListener() {
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
                    hideKeyboard((EditText) username);
                    return true;
                }
                return false;
            }
        });

        password.setOnKeyListener(new View.OnKeyListener() {
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
                    hideKeyboard((EditText) password);
                    return true;
                }
                return false;
            }
        });
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when Let's Party button is clicked
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v) {
                String nameVal= username.getText().toString();
                String pswVal= password.getText().toString();
//                Log.d(TAG, "=====name=======" + nameVal);
//                Log.d(TAG, "=====psw========" + pswVal);
                if (nameVal.equals(USER_NAME) && pswVal.equals(PASS_WORD)) {
                    Intent eventListIntent = new Intent(v.getContext(), EventListActivity.class);
                    //////////////////////////////////////////////////////
                    eventListIntent.putExtra("username", nameVal);
                    //////////////////////////////////////////////////////
                    startActivity(eventListIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Incorrect Username/Password", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
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