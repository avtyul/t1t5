package com.t1t5.pluginator.plugins;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by kvukolov on 03.11.16.
 */
public interface Action extends Serializable{
    void run(Context context);
    String getAction();
}
