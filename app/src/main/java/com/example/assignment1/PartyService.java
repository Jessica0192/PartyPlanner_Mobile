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

public class PartyService extends Service {
    //need chanel to create notification
    private static String Channel_ID = "Party Channel";

    public PartyService() {
    }

    NotificationManager manager = null;
    Timer timer = null;

    @Override
    public void onCreate() {
        Log.d("Party Planer", "Service Created");

        startTimer();

        //real intent
        Intent notificationIntent = new Intent(this, CreateEventActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int piFlag = PendingIntent.FLAG_UPDATE_CURRENT;
        //pass real intent to pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent,piFlag);

        //CharSequence tickerText = "MyTickerText";
        CharSequence ContentTitle = "Party Planner";
        CharSequence ContentText = "Create your event";
        int icon = R.drawable.partylogo;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, Channel_ID)
                .setSmallIcon(icon)
                .setContentTitle(ContentTitle)
                .setContentText(ContentText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Party Planner";
            String description = "This is an app for planing a party.";
            @SuppressLint("WrongConstant")
            NotificationChannel channel = new NotificationChannel(Channel_ID, name, NotificationManagerCompat.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            // Register the channel with the system
            manager.createNotificationChannel(channel);
        }


        manager.notify(1,mBuilder.build());

        //System service
        ConnectivityManager conManager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            Log.d("Party Planer","Network is good");
            //Do something
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Party Planer", "Service Started with id=" + startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void onDestroy() {
        stopTimer();
        Log.d("Party Planer", "Service Destroyed");
        manager.cancel(1);
    }

    private void startTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d("Party Planer", "Timer Task executed");
            }
        };
        timer = new Timer();
        timer.schedule(task, 5000, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

}
