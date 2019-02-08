package com.example.sonakshi.socialcops.notifs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent)
    {
        Intent i = new Intent(context,AlarmRestartService.class);
        context.startService(i);
    }

}
