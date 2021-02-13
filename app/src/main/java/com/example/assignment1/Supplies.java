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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supply);
    }

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

            @Override
            public void onClick(View view) {

                StringBuilder result = new StringBuilder();
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

                SharedPreferences.Editor editor = supplyStorage.edit();

                editor.putString("SupplyItems", result.toString());
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
