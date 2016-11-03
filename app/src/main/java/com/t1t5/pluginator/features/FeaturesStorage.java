package com.t1t5.pluginator.features;

import android.content.Context;

import com.t1t5.pluginator.network.models.Feature;
import com.t1t5.pluginator.plugins.PluginContent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/*
 * Created by kvukolov on 03.11.16.
 */
public class FeaturesStorage {
    public static List<Feature> read(Context context, String filename) throws IOException, ClassCastException, ClassNotFoundException{
        ObjectInputStream objectInputStream = new ObjectInputStream(context.openFileInput(filename));
        List<Feature> plugins = (List<Feature>)objectInputStream.readObject();
        objectInputStream.close();
        return plugins;
    }

    public static void save(List<Feature> features, Context context, String filename) throws IOException{
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));
        objectOutputStream.writeObject(features);
        objectOutputStream.close();
    }
}
