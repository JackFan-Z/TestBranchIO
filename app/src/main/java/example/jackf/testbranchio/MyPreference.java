package example.jackf.testbranchio;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jackf on 2015/10/22.
 */
public class MyPreference {
    public static String EXTRA_CURR_ACCOUNT = "extra_curr_account";

    private final String MY_PREFERENCE_NAME = "my_preference";
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    public MyPreference(Context context) {
        mPrefs = context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    public void putBoolean(String extra, boolean param) {
        mEditor.putBoolean(extra, param);
        mEditor.apply();
    }

    public void putString(String extra, String param) {
        mEditor.putString(extra, param);
        mEditor.apply();
    }

    public boolean getBoolean(String extra) {
        return mPrefs.getBoolean(extra, false);
    }

    public String getString(String extra) {
        return mPrefs.getString(extra, null);
    }
}

