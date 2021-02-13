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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

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
    private CheckBox desser = null;
    private Button save = null;
    private SharedPreferences savedValues = null;
    SharedPreferences menuStorage;

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
        desser = findViewById(R.id.checkbox_dessert);

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
                result.append("Selected Items:");
                if (drink.isChecked()) {
                    result.append("\ndrink");
                }
                if (appetizer.isChecked()) {
                    result.append("\nappetizer");
                }
                if (mainDish.isChecked()) {
                    result.append("\nmainDish");
                }
                if (desser.isChecked()) {
                    result.append("\ndessert");
                }
                //Displaying the message on the toast
                Toast.makeText(getApplicationContext(), "Menu item  selected has been saved! " + result.toString(), Toast.LENGTH_LONG).show();


                //to save the selected items on shared preferences
                SharedPreferences.Editor editor = menuStorage.edit();

                editor.putString("MenuItems", result.toString());
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
