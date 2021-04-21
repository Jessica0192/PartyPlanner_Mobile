package com.example.assignment1;

/*
 * FILE          : My_Custom_Receiver.java
 * PROJECT       : PROG3150 - Assignment #3
 * PROGRAMMER    : Maria Malinina
 * FIRST VERSION : 2020-03-12
 * DESCRIPTION   :
 * This file contains the functionality of the custom broadcast which receives the request
 * when the user sends the invitation to some person. In the application, when the user goes
 * to the invitation sending screen and sends an invitation, the broadcast is triggered and
 * the broadcast starts receiving the request (we see a toast and a log message form both bro
 * adcast class and toast and log message from where we instantiate and broadcast (InviteActi
 * vity)) indicating that the broadcast is being received and after the invitation is sent and
 * we see the toast "Invitation sent", the broadcast was received.
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
 * NAME     :   My_Custom_Receiver
 * PURPOSE :    My_Custom_Receiver class is used to send custom broadcasts. It listens
 * for requests and handles the invitation sending requests.
 */
public class My_Custom_Receiver extends My_Broadcast_Receiver {


    //tag of this class that we're going to log it with
    private static final String TAG = "CustomBroadcastReceiver";


    /*
     * FUNCTION   : onReceive()
     * DESCRIPTION: This function is triggered whenever the user send an invitation
     * to someone.
     * PARAMETERS : Context context,
     *              Intent intent
     * RETURNS    : NONE
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        //If the action received by the current page is sending invitation (
        // as we registered our receiver in InviteActivity), then the broadcast
        //starts to function and we make a toast and a log message indicating
        // that the broadcast is being received and we are broadcasting invitation
        if ("com.example.EXAMPLE_ACTION".equals(intent.getAction())){
            String receivedText = intent.getStringExtra("com.example.EXTRA_TEXT");
            Toast.makeText(context, receivedText, Toast.LENGTH_SHORT).show();
            Log.d(TAG, receivedText);
        }

    }
}

