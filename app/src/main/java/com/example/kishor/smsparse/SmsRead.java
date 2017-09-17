package com.example.kishor.smsparse;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsRead extends BroadcastReceiver {
//    final smsMgr sms = SmsManager.getDefault();
    /**
     * Created by kishor on 16/09/17.
     */

    public static final String TAG=MainActivity.class.getSimpleName();
//    MainActivity smsMain = null;
//
//    public void setMainActivityHandler(MainActivity main)
//    {
//        Log.d(TAG, "main received " + main);
//        smsMain = main;
//    }

    public void onReceive(Context context, Intent intent){
        Log.d(TAG, "IN MESSAGE RECEIVED " + intent.getAction());
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            final Bundle bndl = intent.getExtras();
            SmsMessage[] msgs = null;
            String msgSender = "";
            StringBuilder msgBody = new StringBuilder();
            if(bndl != null)
            {
                try
                {
                    final Object[] pdus = (Object[]) bndl.get("pdus");
                    msgs = new SmsMessage[pdus.length];

                    if(Build.VERSION.SDK_INT >= 23 ){ // MARSHMALLOW
                        for(int i=0; i<msgs.length; i++)
                        {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], bndl.getString("format"));
                            msgSender = msgs[i].getDisplayOriginatingAddress();
                            msgBody.append(msgs[i].getMessageBody());
                        }
                    }
                    else
                    {
                        for(int i=0; i<msgs.length; i++)
                        {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msgSender = msgs[i].getDisplayOriginatingAddress();
                            msgBody.append(msgs[i].getMessageBody());
                        }
                    }

                }
                catch(Exception e)
                {
                    Log.d(TAG, e.toString());
                }

            }
            Log.d(TAG, msgSender + " " + msgBody);
            MainActivity.smsMain.callJsFunction(msgBody.toString(), msgSender);
//            if(smsMain != null)
//            {
//                Log.d(TAG, "CALLING JS FUNCTION ");
//            }
//            context.call
//            ((MainActivity) getContext).callJsFunction(msgBody.toString(), msgSender);
//            MainActivity ma  = new MainActivity();
//            ma.callJsFunction(msgBody.toString(), msgSender);
        }
    }
}
