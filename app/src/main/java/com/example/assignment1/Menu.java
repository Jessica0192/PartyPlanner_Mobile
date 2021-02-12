package com.example.assignment1;

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
                Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            }

        });
    }

    public void backToList(View view)
    {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }


    public void menuSelected(View view)
    {
        Toast.makeText(Menu.this, "Menu Selected", Toast.LENGTH_SHORT).show();
    }
}
