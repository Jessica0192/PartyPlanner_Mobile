package com.example.assignment1;

/*
 * FILE          : My_Broadcast_Receiver.java
 * PROJECT       : PROG3150 - Assignment #3
 * PROGRAMMER    : Maria Malinina
 * FIRST VERSION : 2020-03-12
 * DESCRIPTION   :
 * This file contains the functionality of the system broadcast receiver, which needs to be
 * (and is) declared in the manifest file of this application In this specific example, when-
 * ever the timezone is changed, the user is informed that it is changed, and we log this
 * action. This receiver is always listening for requests to see whether the user changed
 * the timezone or not.
 */


import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/*
 * NAME     :   My_Broadcast_Receiver
 * PURPOSE :    My_Broadcast_Receiver class is used to send system broadcasts. It listens
 * for requests and handles the timezone change action.
 */
public class My_Broadcast_Receiver extends BroadcastReceiver {

    private static final String TAG = "MyBroadcastReceiver";


    /*
     * FUNCTION   : onReceive()
     * DESCRIPTION: This function is triggered whenever the user decides to change
     * the timezone in his device.
     * PARAMETERS : Context context,
     *              Intent intent
     * RETURNS    : NONE
     */
    @Override
    public void onReceive(Context context, Intent intent) {


        //if the timezone action is triggered
            if ((Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())))
            {
                //get the action string indicating the
                String actionString = intent.getAction();

                //get extra string
                String timeZone = intent.getStringExtra("time-zone");

                //log the event
                Log.d(TAG, actionString + "\nonReceive: timezone changed; received " + timeZone);

                //make a toast about the timezone being changed
                Toast.makeText(context, "Timezone changed to " + timeZone + "\n" + actionString, Toast.LENGTH_LONG).show();

            }

        }


    }



