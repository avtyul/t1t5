package com.t1t5.pluginator.plugins;

import android.content.Context;

import com.t1t5.pluginator.plugins.Action;
import com.t1t5.pluginator.plugins.Event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by kvukolov on 03.11.16.
 */
public class PluginStorage {
    public static List<PluginContent> read(Context context, String filename) throws IOException, ClassNotFoundException, ClassCastException{
        ObjectInputStream objectInputStream = new ObjectInputStream(context.openFileInput(filename));
        List<PluginContent> plugins = (List<PluginContent>)objectInputStream.readObject();
        objectInputStream.close();
        return plugins;
    }

    public static void save(List<Plugin> plugins, Context context, String filename)throws IOException{
        List<PluginContent> pluginContents = new ArrayList<>();
        for (Plugin plugin : plugins){
            pluginContents.add(plugin.getPluginContent());
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));
        objectOutputStream.writeObject(pluginContents);
        objectOutputStream.close();
    }
}
