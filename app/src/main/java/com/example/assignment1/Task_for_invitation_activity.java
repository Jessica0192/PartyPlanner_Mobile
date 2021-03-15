//package com.example.assignment1;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.AsyncTask;
//
//
//
//import android.app.Dialog;
//import android.os.Handler;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//public class Task_for_invitation_activity extends AsyncTask<Void, Integer, Void>  {
//
//    Context context;
//    Handler handler;
//    Dialog dialog;
//    TextView txtprogrss;
//    ProgressBar progress;
//    Button btnCancel;
//
//    Task_for_invitation_activity(Context context, Handler handler){
//        this.context=context;
//        this.handler=handler;
//
//    }
//
//    Task_for_invitation_activity(Context context){
//        this.context=context;
//        this.handler=handler;
//    }
//
//    @Override
//    protected void onPreExecute() {
//
//        //progress dialog
//
//        ProgressDialog progressDialog = new ProgressDialog(this, InviteActivity.class);
//
//        super.onPreExecute();
//        // create dialog
//        dialog=new Dialog(context);
//        dialog.setCancelable(true);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.pogressdialog);
//        txtprogrss=(TextView) dialog.findViewById(R.id.txtProgress);
//        progress=(ProgressBar)dialog.findViewById(R.id.pr_bar);
//        btnCancel=(Button)dialog.findViewById(R.id.sendBtn);
//
//        btnCancel.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//
//                Task_for_invitation_activity.this.cancel(true);
//            }
//        });
//
//        //where to show the dialog???
//        dialog.show();
//
//    }
//
//
//    @Override
//    protected Void doInBackground(Void... arg0) {
//
//
//        for (int i = 0; i < 100; i++) {
//            if(isCancelled()){
//                break;
//            }else{
//                Log.e("In Background","current value;"+ i);
//                publishProgress(i);
//
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
//
//        }
//
//        return null;
//    }
//
//    @Override
//    protected void onProgressUpdate(Integer... values) {
//
//
//        super.onProgressUpdate(values);
//
//
//        progress.setProgress(values[0]);
//        txtprogrss.setText("progress update"+ values[0]+"%");
//
//    }
//
//    @Override
//    protected void onPostExecute(Void result) {
//        super.onPostExecute(result);
//
//
//        dialog.dismiss();
//        Toast.makeText(context, "Finished", Toast.LENGTH_LONG).show();
//
//    }
//
//
//
//
//
//}
