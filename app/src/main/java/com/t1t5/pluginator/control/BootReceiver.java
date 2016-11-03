package com.t1t5.pluginator.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by kvukolov on 03.11.16.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startPluginServiceIntent = new Intent(context, PluginService.class);
        context.startService(startPluginServiceIntent);
        Log.d("BOOT COMPLETED", "SERVICE STARTED");
    }
}
