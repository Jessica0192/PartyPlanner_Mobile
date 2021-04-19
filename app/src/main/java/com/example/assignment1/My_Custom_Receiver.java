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

public class My_Custom_Receiver extends My_Broadcast_Receiver {

    private static final String TAG = "CustomBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("com.example.EXAMPLE_ACTION".equals(intent.getAction())){
            String receivedText = intent.getStringExtra("com.example.EXTRA_TEXT");
            Toast.makeText(context, receivedText, Toast.LENGTH_SHORT).show();
        }

//        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
//        {
//            String receivedText = intent.getStringExtra("com.codingflow.EXTRA_TEXT");
//            Toast.makeText(context, receivedText, Toast.LENGTH_SHORT);
////            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
////            if (noConnectivity){
////                Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
////            }else{
////                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
////            }
//        }
        //  if ("com.codingflow.EXAMPLE_ACTION".equals())
//
//        String myMessage = intent.getStringExtra("MyData");
//        Log.d("PartyPlanner", myMessage);




    }



//    @SuppressWarnings("deprecation")
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    public static boolean isAirplaneModeOn(Context context) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            return Settings.System.getInt(context.getContentResolver(),
//                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
//        } else {
//            return Settings.Global.getInt(context.getContentResolver(),
//                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
//        }
//    }
}

//public class My_Broadcast_Receiver_Airplane extends My_Broadcast_Receiver {
//
//    private static final String TAG = "AirplaneReceiver";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())){
//
//            String actionString = intent.getAction();
//            Toast.makeText(context, actionString, Toast.LENGTH_LONG).show();
//
//            int isOn = 0;
//            isOn = Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0);
//
//            if (isOn !=0)
//            {
//                Log.d(TAG, "onReceive: Airplane Mode Enabled");
//            }
//            else
//            {
//                Log.d(TAG, "onReceive: Airplane Mode Disabled");
//            }
//
//            Log.d("AirplaneMode", "Service state changed");
//            boolean airplane_active = isAirplaneModeOn(context);
//        }
//
//    }
//
//
//
//    @SuppressWarnings("deprecation")
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    public static boolean isAirplaneModeOn(Context context) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            return Settings.System.getInt(context.getContentResolver(),
//                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
//        } else {
//            return Settings.Global.getInt(context.getContentResolver(),
//                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
//        }
//    }
//}