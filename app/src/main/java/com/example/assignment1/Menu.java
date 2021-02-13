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

public class Menu extends AppCompatActivity {

    private TextView menu = null;
    private CheckBox drink = null;
    private CheckBox appetizer = null;
    private CheckBox mainDish = null;
    private CheckBox desser = null;
    private Button save = null;
    private SharedPreferences savedValues = null;
    SharedPreferences menuStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }

    public void addListenerOnButtonClick() {
        //Getting instance of CheckBoxes and Button from the activty_main.xml file
        drink = findViewById(R.id.checkbox_drink);
        appetizer = findViewById(R.id.checkbox_appetizer);
        mainDish = findViewById(R.id.checkbox_mainDish);
        desser = findViewById(R.id.checkbox_dessert);

        Button saveBtn = findViewById(R.id.saveBtn);
        //Applying the Listener on the Button click
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                StringBuilder result = new StringBuilder();
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
                    result.append("\ndesser");
                }
                //Displaying the message on the toast
                Toast.makeText(getApplicationContext(), "Menu item  selected has been saved: " + result.toString(), Toast.LENGTH_LONG).show();

                menuStorage = getSharedPreferences("MenuSelected", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = menuStorage.edit();

                editor.putString("MenuItems", result.toString());
                editor.apply();
            }

        });
    }

    public void backToEvent(View view)
    {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

}
