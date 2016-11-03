package com.t1t5.pluginator.plugins;

import android.content.Intent;
import android.content.IntentFilter;

import java.io.Serializable;

/*
 * Created by kvukolov on 03.11.16.
 */
public class Event implements Serializable {
    private String intentFilter;

    public Event(String intentFilter) {
        this.intentFilter = intentFilter;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Event)){
            return false;
        }
        Event event = (Event)o;
        return intentFilter.equals(event.getIntentFilter());
    }

    public String getIntentFilter() {
        return intentFilter;
    }
}
