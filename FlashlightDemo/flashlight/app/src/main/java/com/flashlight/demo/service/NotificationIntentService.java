package com.flashlight.demo.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.flashlight.demo.activity.CompassActivity;
import com.flashlight.demo.activity.HomeActivity;
import com.flashlight.demo.handler.FlashModeHandler;
import com.flashlight.demo.handler.NotificationHandler;
import com.flashlight.demo.util.MyConstants;
import com.flashlight.demo.util.ToastUtil;

public class NotificationIntentService extends IntentService {
    String TAG = "NotificationIntentService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public NotificationIntentService() {
        super("notificationIntentService");
    }


    /**
     * @param intent Handle Intent when click icon at status bar
     */
    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        Log.i(TAG, "onHandleIntent");

        if (intent==null){
            ToastUtil.longToast(this, "Something went wrong");
            return;
        }
        switch (intent.getAction()) {
            case MyConstants.NOTIFICATION_ACTION_ICON:
                Log.i(TAG, "icon");

                Handler icon = new Handler();
                icon.post(() -> {
                    Intent launchIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);

                    Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    sendBroadcast(it);
                });
                break;

            case MyConstants.NOTIFICATION_ACTION_FLASH:
                Log.i(TAG, "flash");

                Handler toggle = new Handler(Looper.getMainLooper());
                toggle.post(() -> {

                    Intent launchIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);

                    Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    sendBroadcast(it);
                });
                break;

            case MyConstants.NOTIFICATION_ACTION_COMPASS:
                Log.i(TAG, "compass");

                Handler compass = new Handler(Looper.getMainLooper());
                compass.post(() -> {
                    //MyTracking.sendCustomAction("STATUS_BAR", TrackingConstants.ACTION.STATUS_BAR_COMPASS);

                    FlashModeHandler.getInstance().setLaunch("COMPASS");
                    Intent launchIntent = new Intent(getApplicationContext(), CompassActivity.class);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);

                    Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    sendBroadcast(it);
                });
                break;

            case MyConstants.NOTIFICATION_ACTION_WIFI:
                Log.i(TAG, "wifi");

                Handler wifi = new Handler(Looper.getMainLooper());
                wifi.post(() -> {
                    //MyTracking.sendCustomAction("STATUS_BAR", TrackingConstants.ACTION.STATUS_BAR_WIFI);

                    WifiManager wifi1 = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if (wifi1.isWifiEnabled()) {
                        FlashModeHandler.getInstance().setWifiOn(false);
                        wifi1.setWifiEnabled(false);
                    } else {
                        FlashModeHandler.getInstance().setWifiOn(true);
                        wifi1.setWifiEnabled(true);
                    }

                    NotificationHandler handler = new NotificationHandler(getApplicationContext());
                    handler.addNotify();
                });
                break;

            case MyConstants.NOTIFICATION_ACTION_GPRS:
                Log.i(TAG, "gprs");
                //MyTracking.sendCustomAction("STATUS_BAR", TrackingConstants.ACTION.STATUS_BAR_DATA);

                Handler gprs = new Handler(Looper.getMainLooper());
                gprs.post(() -> {
                    Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    sendBroadcast(it);
                });
                break;

            default:
                break;
        }
    }
}
