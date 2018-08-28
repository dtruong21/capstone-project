package dalker.cmtruong.com.app.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.model.User;
import timber.log.Timber;

/**
 * Preferences is used to save the session
 *
 * @author davidetruong
 * @version 1.0
 * @since 31th July, 2018
 */
public class PreferencesHelper {

    public static void saveUserSession(Context context, String userSession) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.user_session), userSession);
        editor.apply();
    }

    public static void saveDocumentReference(Context context, String reference) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.reference), reference);
        editor.apply();
    }

    public static String getDocumentReference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.reference), "");
    }

    public static String getUserSession(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.user_session), "");
    }

    public static void deleteUserSession(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().clear().apply();
    }

    public static void saveDevideID(Context context, String device) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.device_id), device);
        editor.apply();
    }

    public static String getDeviceID(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.device_id), "");
    }

    public static void saveUserList(Context context, String userString) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.user_list_session), userString);
        editor.apply();
    }

    public static ArrayList<User> getUserList(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString(context.getString(R.string.user_list_session), "");
        ArrayList<User> users;
        Gson gson = new Gson();
        Type type = new TypeToken<List<User>>() {
        }.getType();
        users = gson.fromJson(json, type);
        Timber.d("My preferences user %s", users.toString());
        return users;
    }
}
