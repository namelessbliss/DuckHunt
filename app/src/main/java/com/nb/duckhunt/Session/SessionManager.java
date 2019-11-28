package com.nb.duckhunt.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.nb.duckhunt.ui.LoginActivity;

import java.util.HashMap;

public class SessionManager {

    //Estado de session
    boolean session = false;

    //Shared Preferences reference
    SharedPreferences pref;

    //Editor reference for shared preference
    SharedPreferences.Editor editor;

    //Contexto
    Context context;


    // Shared preferences file name
    private static final String PREFER_NAME = "session";

    // All Shared Preferences  Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String KEY_ID = "id";

    public static final String KEY_NICK = "nick";

    public static final String KEY_DUCKS_HUNTED = "patos";

    public SessionManager(Context _context) {
        this.context = _context;
        pref = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserSession(String id, String nick, int patos) {

        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        //Storin name in pref
        editor.putString(KEY_NICK, nick);

        //Storin id in pref
        editor.putString(KEY_ID, id);

        editor.putInt(KEY_DUCKS_HUNTED, patos);

        //commit changes
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * if else it will redirect user to login page
     * Else do anything
     */

    public boolean isNotLogin() {
        //Check login status
        if (!this.isUserLoggedIn()) {

            //user not logged in redirect him to Login Activity
            Intent intent = new Intent(context, LoginActivity.class);

            //closing all the activities from stack
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //add new flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Starting Login Activity
            context.startActivity(intent);

            return true;
        }
        return false;
    }

    public boolean isLogin() {
        //Check login status
        if (this.isUserLoggedIn()) {

            return true;
        }
        return false;
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {

        // use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<>();

        //user ducks
        user.put(KEY_DUCKS_HUNTED, pref.getInt(KEY_DUCKS_HUNTED, 0) + "");

        //user name
        user.put(KEY_NICK, pref.getString(KEY_NICK, ""));

        //user id
        user.put(KEY_ID, pref.getString(KEY_ID, ""));

        return user;
    }

    /**
     * Clear session details
     */

    public void logoutUser() {
        //Clearing all user data from shared preferences
        editor.clear();
        editor.commit();

        //After logout redirect user to login Activity
        Intent intent = new Intent(context, LoginActivity.class);

        //closing all activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //add new flag to start new activities
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //start login activity
        context.startActivity(intent);
    }

    // Check for login
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }


}

