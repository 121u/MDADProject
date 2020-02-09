package com.example.mdadproject.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "username";
    static final String PREF_QUEUE_NUMBER= "queue_no";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
//        editor.clear(); //clear all stored data
        editor.remove(PREF_USER_NAME);
        editor.commit();
    }

    public static void setPrefQueueNumber(Context ctx, int queue_no)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_QUEUE_NUMBER, queue_no);
        editor.commit();
    }

    public static int getPrefQueueNumber(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(PREF_QUEUE_NUMBER, 0);
    }

    public static void clearPrefQueueNumer(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
//        editor.clear(); //clear all stored data
        editor.remove(PREF_QUEUE_NUMBER);
        editor.commit();
    }
}
