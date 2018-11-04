package id.ac.astra.polman.projectkamus.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import id.ac.astra.polman.projectkamus.R;

/**
 * Created by Jihad044 on 30/06/2018.
 */

public class KamusPreference {

    SharedPreferences prefs;
    Context context;

    public KamusPreference(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public void setFirstRun(Boolean input){

        SharedPreferences.Editor editor = prefs.edit();
        String key = context.getResources().getString(R.string.kamus_pref);
        editor.putBoolean(key,input);
        editor.commit();
    }

    public Boolean getFirstRun(){
        String key = context.getResources().getString(R.string.kamus_pref);
        return prefs.getBoolean(key, true);
    }

}
