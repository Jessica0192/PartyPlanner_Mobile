package com.example.assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EventListActivity extends MainActivity {

    public static final String TAG = "EventListActivity";

    Button addEventBtn = null;

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
        setContentView(R.layout.activity_event_list);
        addEventBtn = (Button) findViewById(R.id.btnAddEvent);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION: onClick
             * DESCRIPTION:
             *      This function is called when Add Event button is clicked
             * PARAMETER:
             *      View v: the view within the AdapterView that was clicked
             * RETURNS:
             *      void: there's no return value
             */
            @Override
            public void onClick(View v) {
                Intent createEventIntent = new Intent(v.getContext(), CreateEventActivity.class);
                SharedPreferences guestSp = getApplicationContext().getSharedPreferences("GuestPrefs", Context.MODE_PRIVATE);
                SharedPreferences menuSp = getApplicationContext().getSharedPreferences("MenuSelected", Context.MODE_PRIVATE);
                SharedPreferences supplySp = getApplicationContext().getSharedPreferences("SupplySelected", Context.MODE_PRIVATE);
                SharedPreferences newEvent = getApplicationContext().getSharedPreferences("Saved Values", Context.MODE_PRIVATE);

                menuSp.edit().clear().apply();
                supplySp.edit().clear().apply();
                guestSp.edit().clear().apply();
                newEvent.edit().clear().apply();
                startActivity(createEventIntent);
            }
        });
    }
}