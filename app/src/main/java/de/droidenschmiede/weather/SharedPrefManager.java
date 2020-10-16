package de.droidenschmiede.weather;

import android.content.SharedPreferences;

public class SharedPrefManager {


    public static final String PREFNAME = "prefs";

    public static final String NAME_UNIQUEID = "uniqueid";

    public MainActivity m;
    public SharedPreferences pref;

    public SharedPrefManager(MainActivity m){
        this.m = m;

        pref = m.getSharedPreferences(PREFNAME, 0);
    }

    public void saveSharedInt(String name, int value){

        SharedPreferences.Editor ed = pref.edit();
        ed.putInt(name, value);
        ed.commit();
    }

    public int getSharedInt(String name, int defaultValue){
        return pref.getInt(name, defaultValue);
    }

    public void setUniqueID(int id){
        saveSharedInt(NAME_UNIQUEID, id);
    }

    public int getUniqueID(){
        return getSharedInt(NAME_UNIQUEID, -1);
    }
}

