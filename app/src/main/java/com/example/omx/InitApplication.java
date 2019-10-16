package com.example.omx;

        import android.app.Application;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.preference.PreferenceManager;


/**
 * Created by anupamchugh on 01/03/18.
 */

public class InitApplication extends Application {
    public static final String NIGHT_MODE = "NIGHT_MODE";
    public static final String NOTIFY_MODE = "NOTIFY_MODE";
    private boolean isNightModeEnabled = false;
    private boolean isNotificationEnabled = false;
   // SharedPreferenceClass sharedPreferenceClass;
    private static InitApplication singleton = null;

    public static InitApplication getInstance() {

        if(singleton == null)
        {
            singleton = new InitApplication();
        }
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.isNightModeEnabled = mPrefs.getBoolean(NIGHT_MODE, false);
        this.isNotificationEnabled = mPrefs.getBoolean(NOTIFY_MODE, false);
       // sharedPreferenceClass = new SharedPreferenceClass(InitApplication.this);
    }

    public static Context getAppContext() {
        return singleton.getApplicationContext();
    }

    public boolean isNightModeEnabled() {
        return isNightModeEnabled;
    }
    public boolean isNotificationEnabled() {
        return isNotificationEnabled;
    }

    public void setIsNightModeEnabled(boolean isNightModeEnabled) {
        this.isNightModeEnabled = isNightModeEnabled;

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(NIGHT_MODE, isNightModeEnabled);
        editor.apply();
    }

    public void setisNotificationEnabled(boolean isNotificationEnabled){
        this.isNotificationEnabled = isNotificationEnabled;
        //sharedPreferenceClass.setValue_boolean(NOTIFY_MODE,isNotificationEnabled);

    }
}
