package com.android.pushnotifications;

import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import android.content.DialogInterface;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends Activity {

    private static final String LOG_TAG = "SMSReceiver";
    public static final int NOTIFICATION_ID_RECEIVER = 0x1221;
    static final String ACTION = "android.provider.Telephony.SMS_RECEIVER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(ACTION);
        this.registerReceiver(mRegistrationBroadcastReceiver, filter);

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        //if play service is not available
        if(ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }
    }



    //Creating a broadcast receiver for gcm registration
    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        //When the broadcast received
        //We are sending the broadcast from GCMRegistrationIntentService
        @Override
        public void onReceive(Context context, Intent intent) {
            //If the broadcast has received with success
            //that means device is registered successfully
            if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                //Getting the registration token from the intent
                String token = intent.getStringExtra("token");
                //Displaying the token as toast
                Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();



//                String action = intent.getAction();
//                if(ACTION.equals(action)){
//                }
                //if the intent is not with success then displaying error messages
            } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
            }
        }
    };

    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }


    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
}
