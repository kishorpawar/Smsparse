package com.example.kishor.smsparse;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TAG=MainActivity.class.getSimpleName();
    public static MainActivity smsMain;

    public MainActivity(){
        smsMain = this;
    }

    public static Context getContext(){
        return smsMain;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(this.canReadSms()) {
            Log.i(TAG, "onCreate: ");
            continueWithPermissions();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "IN onResume");
        if(ContextCompat.checkSelfPermission(smsMain, Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            this.continueWithPermissions();
        }
    }

    private void continueWithPermissions() {
        WebView wbView = (WebView) findViewById(R.id.webView1);
        wbView.getSettings().setJavaScriptEnabled(true);

        wbView.setWebViewClient(new WebViewClient() {
        });
        //        wbView.loadUrl("http://www.google.com");
        wbView.loadUrl("file:///android_asset/sms.html");
    }

    public void callJsFunction(String sms, String sender){
        Log.d(TAG,  ">>>>> "+ sms + " " + sender);
        WebView smsview = (WebView) findViewById(R.id.webView1);
        smsview.loadUrl("javascript:showSMS('" + sms + "','" + sender + "')");
    }

    public  void exitApp(){
        System.exit(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean canReadSms(){
        if(ContextCompat.checkSelfPermission(smsMain, Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED)
        {
            Log.d(TAG, "PERMISSION GRANTED");
            return true;
        }
        else{
            Log.d(TAG, "PERMISSION Denied");
            Toast.makeText(smsMain, "Need permissions to read sms", Toast.LENGTH_SHORT).show();
            if(! shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                this.showMessageOKCancel("Trust us, we just reading your sms to authenticate you.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(
                                        new Intent(
                                                android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                            }
                        });
            }
            else
            {
                ActivityCompat.requestPermissions(smsMain,
                        new String[]{Manifest.permission.READ_SMS}, 123);
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        Log.d(TAG, String.valueOf(requestCode));
        Log.d(TAG, permissions.toString());
        Log.d(TAG, grantResults.toString());
        if(requestCode == 123 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            this.continueWithPermissions();
        }
        else
        {
            Toast.makeText(smsMain, "Need permissions to read sms", Toast.LENGTH_SHORT).show();
            this.canReadSms();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(smsMain)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("Allow", okListener)
                .setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        smsMain.exitApp();
                    }
                })
                .create()
                .show();
    }
}
