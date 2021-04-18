package com.example.assignment1;

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

public class My_Broadcast_Receiver extends BroadcastReceiver {

    private static final String TAG = "MyBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        // throw new UnsupportedOperationException("Not yet implemented");
        if ((Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction()))|| (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())))
        {
            if ((Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())))
            {
                String actionString = intent.getAction();
                Toast.makeText(context, actionString, Toast.LENGTH_LONG).show();
                String timeZone = intent.getStringExtra("time-zone");

                Log.d(TAG, "onReceive: " + timeZone);
            }else{

                String actionString = intent.getAction();
                Toast.makeText(context, actionString, Toast.LENGTH_LONG).show();

                int isOn = 0;
                isOn = Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0);

                if (isOn !=0)
                {
                    Log.d(TAG, "onReceive: Airplane Mode Enabled");
                }
                else
                {
                    Log.d(TAG, "onReceive: Airplane Mode Disabled");
                }


            }
        }
        // }

//
//
//        if (Intent.ACTION_TIME_CHANGED.equals(intent.getAction()))
//        {
//            Toast.makeText(context, "Time zone changed successfully", Toast.LENGTH_LONG).show();
//        }
//
//
//        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
//        {
//            Toast.makeText(context,"Connectivity mode changed", Toast.LENGTH_LONG).show();
//        }
    }


}



