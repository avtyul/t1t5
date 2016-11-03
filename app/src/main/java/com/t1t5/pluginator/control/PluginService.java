package com.t1t5.pluginator.control;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.t1t5.pluginator.R;
import com.t1t5.pluginator.plugins.Plugin;
import com.t1t5.pluginator.plugins.PluginContent;
import com.t1t5.pluginator.plugins.PluginStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by kvukolov on 03.11.16.
 */
public class PluginService extends Service {
    private static Boolean running = false;
    public static Boolean isRunning(){
        return running;
    }
    List<Plugin> plugins;
    String filename;

    private void savePlugins(){
        try{
            PluginStorage.save(plugins, getApplicationContext(), filename);
        }
        catch (IOException e){
            Log.e("CANNOT WRITE PLUGINS", e.getMessage());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PluginServiceBinder() {
            @Override
            public List<Plugin> getPlugins() {
                return plugins;
            }

            @Override
            public void addPlugin(Plugin plugin) {
                if(!plugins.contains(plugin)){
                    plugins.add(plugin);
                }
                savePlugins();
            }

            @Override
            public void enablePlugin(Plugin plugin) {
                int pluginIndex = plugins.indexOf(plugin);
                if(pluginIndex >= 0){
                    plugin = plugins.get(pluginIndex);
                    plugin.enable();
                    savePlugins();
                }
            }

            @Override
            public void disablePlugin(Plugin plugin) {
                int pluginIndex = plugins.indexOf(plugin);
                if(pluginIndex >= 0){
                    plugin = plugins.get(pluginIndex);
                    plugin.disable();
                    savePlugins();
                }
            }

            @Override
            public void deletePlugin(Plugin plugin) {
                if(plugins.contains(plugin)){
                    plugin.disable();
                }
                plugins.remove(plugin);
                savePlugins();
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        filename = getApplicationContext().getString(R.string.PLUGINS_STORAGE);
        plugins = new ArrayList<>();
        try{
            List<PluginContent> pluginContents = PluginStorage.read(getApplicationContext(), filename);
            for (PluginContent pluginContent: pluginContents){
                Plugin plugin = new Plugin(getApplicationContext(), pluginContent);
                plugins.add(plugin);
                if(pluginContent.getEnabled()){
                    plugin.enable();
                }
            }
        }
        catch (Exception e){
            Log.e("CANNOT READ PLUGINS", e.getMessage());
        }
        running = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
    }
}
