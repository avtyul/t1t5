package com.t1t5.pluginator.control;

import android.os.Binder;

import com.t1t5.pluginator.plugins.Plugin;

import java.util.List;

/*
 * Created by kvukolov on 03.11.16.
 */
public abstract class PluginServiceBinder extends Binder {
    public abstract List<Plugin> getPlugins();
    public abstract void addPlugin(Plugin plugin);
    public abstract void enablePlugin(Plugin plugin);
    public abstract void disablePlugin(Plugin plugin);
    public abstract void deletePlugin(Plugin plugin);
}
