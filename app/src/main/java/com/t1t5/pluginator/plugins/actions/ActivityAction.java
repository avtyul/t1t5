package com.t1t5.pluginator.plugins.actions;

import android.content.Context;
import android.content.Intent;

import com.t1t5.pluginator.plugins.Action;

/*
 * Created by kvukolov on 03.11.16.
 */
public class ActivityAction implements Action {
    private String actionString;

    public ActivityAction(String actionString) {
        this.actionString = actionString;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ActivityAction)){
            return false;
        }
        ActivityAction activityAction = (ActivityAction)o;
        return (actionString.equals(activityAction.actionString));
    }

    @Override
    public void run(Context context) {
        Intent intent = new Intent(actionString);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public String getAction() {
        return actionString;
    }
}
