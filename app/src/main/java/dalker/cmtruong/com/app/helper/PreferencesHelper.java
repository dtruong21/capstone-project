package dalker.cmtruong.com.app.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.model.User;

/**
 * Preferences is used to save the session
 *
 * @author davidetruong
 * @version 1.0
 * @since 31th July, 2018
 */
public class PreferencesHelper {

    public static void saveUserSession(Context context, User user) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(context.getString(R.string.user_session), json);
        editor.apply();
    }

    public static String getUserSession(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.user_session), "");
    }
}
