/*
 * FILE          : PartyService.java
 * PROJECT       : PROG3150 - Assignment #3
 * PROGRAMMER    : Hoda Akrami
 * FIRST VERSION : 2020-04-19
 * DESCRIPTION   : This file contains the components and functions to make a service for the party planner application
 *                 It makes a noise on the background and log the time.
 *                 Also, it has notification to remind user to about the event.
 */
package com.example.assignment1;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Timer;
import java.util.TimerTask;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

/*
 * NAME    :    PartyService
 * PURPOSE :    PartyService class contains the component to make a noise on the background and log the time.
 *              Also, it has notification to remind user to about the event.
 */
public class PartyService extends Service
{
    //need chanel to create notification
    private static String Channel_ID = "Party Channel";

    //constructor
    public PartyService() {
    }

    NotificationManager manager = null;
    Timer timer = null;

    /*  -- Function Header Comment
   Name	   :   onCreate()
   Purpose :   This function is called when the service runs and notification creates.
   Inputs  :	NONE
   Outputs :	log messages
   Returns :	NONE
   */
    @Override
    public void onCreate()
    {
        Log.d("Party Planer", "Service Created");

        //to start the timer
        startTimer();

        //real intent
        Intent notificationIntent = new Intent(PartyService.this, CreateEventActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int piFlag = PendingIntent.FLAG_UPDATE_CURRENT;
        //pass real intent to pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent,piFlag);

        //the content of notification
        CharSequence ContentTitle = "Party Planner";
        CharSequence ContentText = "Create your event";
        int icon = R.drawable.partylogo;

        //to make a notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, Channel_ID)
                .setSmallIcon(icon)
                .setContentTitle(ContentTitle)
                .setContentText(ContentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //to check if we are able to build the party app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "Party Planner";
            String description = "This is an app for planing a party.";
            @SuppressLint("WrongConstant")
            NotificationChannel channel = new NotificationChannel(Channel_ID, name, NotificationManagerCompat.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            // Register the channel with the system
            manager.createNotificationChannel(channel);
        }

        //it will update the notification if already exists
        manager.notify(1,mBuilder.build());

        //System service for network connectivity
        ConnectivityManager conManager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        //to check if able to make a connection
        if (netInfo != null && netInfo.isConnected())
        {
            Log.d("Party Planer","Network is good");
        }
    }

    /*  -- Function Header Comment
   Name	    :   onStartCommand(Intent intent, int flags, int startId)
   Purpose  :   To start the service
   Inputs	:	Intent   intent
                int     flags
                int     startId
   Outputs	:	log a message
   Returns	:	NONE
   */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("Party Planer", "Service Started with id=" + startId);
        return START_STICKY;
    }


    /*  -- Function Header Comment
   Name	    :   onBind(Intent intent)
   Purpose  :   To start the service
   Inputs	:	Intent   intent
   Outputs	:	NONE
   Returns	:	NONE
   */
    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not implemented");
    }


    /*  -- Function Header Comment
   Name	    :   onDestroy()
   Purpose  :   To destroy the service
   Inputs	:	NONE
   Outputs	:	log a message
   Returns	:	NONE
   */
    @Override
    public void onDestroy()
    {
        stopTimer();
        Log.d("Party Planer", "Service Destroyed");
        manager.cancel(1);
    }


    /*  -- Function Header Comment
   Name	    :   startTimer()
   Purpose  :   To start a timer when service runs
   Inputs	:	NONE
   Outputs	:	log the time
   Returns	:	NONE
   */
    private void startTimer()
    {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d("Party Planer", "Timer Task executed");
            }
        };
        timer = new Timer();
        timer.schedule(task, 5000, 1000);
    }


    /*  -- Function Header Comment
   Name	    :   stopTimer()
   Purpose  :   To stop a timer when service stops
   Inputs	:	NONE
   Outputs	:	None
   Returns	:	NONE
   */
    private void stopTimer()
    {
        if (timer != null) {
            timer.cancel();
        }
    }
}
