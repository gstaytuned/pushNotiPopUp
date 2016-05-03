package com.android.pushnotifications;

import android.content.Context;

/**
 * Created by ger on 5/3/2016 AD.
 */
public class DataManager {

    private static DataManager instance;

    public static  DataManager getInstance() {
        if (instance == null)
            instance = new DataManager();
        return instance;
    }

    private Context mContext;

    private DataManager() {
        mContext = Contextor.getInstance().getContext();
    }

    private String serverMessage;

    public String getServerMessage() {
        return serverMessage;
    }

    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
    }
}
