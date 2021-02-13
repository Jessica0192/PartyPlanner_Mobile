/*
 * FILE          : Supplies.java
 * PROJECT       : PROG3150 - Assignment #1
 * PROGRAMMER    : Hoda Akrami
 * FIRST VERSION : 2020-02-10
 * DESCRIPTION   : This file contains the functionality behind the supply.xml file.
 *                 When the user is presented with this screen, he had a list view of supplies,
 *                 he can select the item that he wants to use in his party and save it.
 */

package com.example.assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

/*
 * NAME     :    Supplies
 * PURPOSE :    Supplies class contains the functionality behind the supply.xml file.
 *              It contains functions for loading the page, saving the selected items on the shared preferences,
 *              and getting back to the activity page.
 */
public class Supplies extends AppCompatActivity{

    private TextView supply = null;
    private CheckBox balloons = null;
    private CheckBox cake = null;
    private CheckBox flowers = null;
    private CheckBox cups = null;
    private CheckBox cutlery = null;
    private CheckBox candle = null;
    private CheckBox invitations = null;
    private Button save = null;
    private SharedPreferences savedValues = null;
    SharedPreferences supplyStorage;

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
    }

    /*  -- Function Header Comment
	Name	:   addListenerOnButtonClick()
	Purpose :   To save the user selection and store it on shared preferences
	Inputs	:	NONE
	Outputs	:	The popup message to present what items selected
	Returns	:	NONE
    */
    public void addListenerOnButtonClick() {
        //Getting instance of CheckBoxes and Button from the activty_main.xml file
        balloons = findViewById(R.id.checkbox_balloons);
        cake = findViewById(R.id.checkbox_cake);
        flowers = findViewById(R.id.checkbox_flowers);
        cups = findViewById(R.id.checkbox_cups);
        cutlery = findViewById(R.id.checkbox_cutlery);
        candle = findViewById(R.id.checkbox_candle);
        invitations = findViewById(R.id.checkbox_invitations);

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
                result.append("Selected Items:");
                if (balloons.isChecked()) {
                    result.append("\nballoons");
                }
                if (cake.isChecked()) {
                    result.append("\ncake");
                }
                if (flowers.isChecked()) {
                    result.append("\nflowers");
                }
                if (cups.isChecked()) {
                    result.append("\ncups");
                }
                if (cutlery.isChecked()) {
                    result.append("\ncutlery");
                }
                if (candle.isChecked()) {
                    result.append("\ncandle");
                }
                if (invitations.isChecked()) {
                    result.append("\ninvitations");
                }
                //Displaying the message on the toast
                Toast.makeText(getApplicationContext(), "Supply items selected has been saved: " + result.toString(), Toast.LENGTH_LONG).show();

                supplyStorage = getSharedPreferences("SupplySelected", Context.MODE_PRIVATE);

                //to save the selected items on shared preferences
                SharedPreferences.Editor editor = supplyStorage.edit();

                editor.putString("SupplyItems", result.toString());
                editor.apply();
            }

        });
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
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }
}
