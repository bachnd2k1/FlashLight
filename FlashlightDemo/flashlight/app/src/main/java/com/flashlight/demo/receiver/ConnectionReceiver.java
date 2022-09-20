package com.flashlight.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*if (FlashModeHandler.getInstance().isGPRS()) {
            NotificationHandler handler = new NotificationHandler(context);
            handler.addNotify();
        }*/
    }
}
