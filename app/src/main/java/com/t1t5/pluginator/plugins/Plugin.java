package com.t1t5.pluginator.plugins;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.Serializable;

/*
 * Created by kvukolov on 03.11.16.
 */

public class Plugin {
    private BroadcastReceiver receiver;
    private Context context;
    private PluginContent pluginContent;

    public Plugin(Context context, PluginContent pluginContent) {
        this.context = context;
        this.pluginContent = pluginContent;
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("EVENT_RECEIVED", intent.getAction());
                Plugin.this.pluginContent.getAction().run(context);
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Plugin)){
            return false;
        }
        Plugin plugin = (Plugin)o;
        return (this.pluginContent.equals(plugin.pluginContent));
    }

    public PluginContent getPluginContent() {
        return pluginContent;
    }

    public Boolean enabled(){
        return pluginContent.getEnabled();
    }

    public void enable(){
        pluginContent.setEnabled(true);
        context.registerReceiver(receiver, new IntentFilter(pluginContent.getEvent().getIntentFilter()));
    }

    public void disable(){
        pluginContent.setEnabled(false);
        context.unregisterReceiver(receiver);
    }
}
