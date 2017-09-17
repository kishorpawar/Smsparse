package com.example.kishor.smsparse;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        canReadSms();
        Log.i(TAG, "onCreate: ");
        WebView wbView = (WebView) findViewById(R.id.webView1);
        wbView.getSettings().setJavaScriptEnabled(true);

        wbView.setWebViewClient(new WebViewClient(){
//            public void onPageFinished(WebView wv, String url)
//            {
//                SmsRead smsRead = new SmsRead();
//                smsRead.setMainActivityHandler((MainActivity) activity);
//                IntentFilter callInterceptorIntentFilter =
//                        new IntentFilter("android.intent.action.ANY_ACTION");
//                registerReceiver(smsRead, callInterceptorIntentFilter);
//            }
        });
//        wbView.loadUrl("http://www.google.com");
        wbView.loadUrl("file:///android_asset/sms.html");
    }

    public void callJsFunction(String sms, String sender){
        Log.d(TAG,  ">>>>> "+ sms + " " + sender);
        WebView smsview = (WebView) findViewById(R.id.webView1);
        smsview.loadUrl("javascript:showSMS(" + sms + "," + sender + ")");
    }

    public boolean canReadSms(){
        if(ContextCompat.checkSelfPermission(smsMain, Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else{
            Toast.makeText(smsMain, "Need permissions to read sms", Toast.LENGTH_SHORT);
            System.exit(0);
//            ActivityCompat.requestPermissions(smsMain,
//                    new String[]{Manifest.permission.READ_SMS},
//                    );
        }

        return false;
    }
}
