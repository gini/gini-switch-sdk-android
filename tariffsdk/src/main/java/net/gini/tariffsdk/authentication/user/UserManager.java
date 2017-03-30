package net.gini.tariffsdk.authentication.user;


import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.VisibleForTesting;

import net.gini.tariffsdk.authentication.models.UserCredentials;

import java.util.UUID;

public class UserManager {

    private UserManager() {

    }

    @VisibleForTesting
    static final String USER_KEY_ID = "USER_KEY_ID";

    @VisibleForTesting
    static final String USER_SHARE_PREFERENCES = "USER_SHARE_PREFERENCES";

    public static UserCredentials getUserCredentials(final Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String userId;
        if(userCredentialsExist(context)) {
            userId = sharedPreferences.getString(USER_KEY_ID, "");
        } else {
            userId = generateUserId();
            storeUserId(context, userId);
        }
        //TODO
        return new UserCredentials(userId, "TODO");
    }

    @SuppressLint("ApplySharedPref")
    private static void storeUserId(Context context, final String userId) {
        getSharedPreferences(context).edit().putString(USER_KEY_ID, userId).commit();
    }

    private static String generateUserId() {
        return UUID.randomUUID().toString();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(USER_SHARE_PREFERENCES, MODE_PRIVATE);
    }

    public static boolean userCredentialsExist(Context context) {
        return getSharedPreferences(context).contains(USER_KEY_ID);
    }

}
