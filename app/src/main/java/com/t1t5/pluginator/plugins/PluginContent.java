package com.t1t5.pluginator.plugins;

import java.io.Serializable;

/**
 * Created by kvukolov on 03.11.16.
 */
public class PluginContent implements Serializable {
    private Event event;
    private Action action;
    private Boolean enabled = false;

    public PluginContent(Event event, Action action, Boolean enabled) {
        this.event = event;
        this.action = action;
        this.enabled = enabled;
    }

    public PluginContent(Event event, Action action) {
        this.event = event;
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof PluginContent)){
            return false;
        }
        PluginContent content = (PluginContent)o;
        return (event.equals(content.event) && action.equals(content.action) && enabled.equals(content.enabled));
    }

    public Event getEvent() {
        return event;
    }

    public Action getAction() {
        return action;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
