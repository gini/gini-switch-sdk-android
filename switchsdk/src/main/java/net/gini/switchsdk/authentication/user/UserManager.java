package net.gini.switchsdk.authentication.user;


import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import net.gini.switchsdk.authentication.models.UserCredentials;

import java.util.UUID;

public class UserManager {

    @VisibleForTesting
    static final String USER_KEY_EMAIL = "USER_KEY_EMAIL";
    static final String USER_KEY_PASSWORD = "USER_KEY_PASSWORD";
    @VisibleForTesting
    static final String USER_SHARE_PREFERENCES = "USER_SHARE_PREFERENCES";
    private final String mDomain;
    private SharedPreferences mSharedPreferences;

    public UserManager(@NonNull final Context context, @NonNull String domain) {
        mSharedPreferences = context.getSharedPreferences(USER_SHARE_PREFERENCES, MODE_PRIVATE);
        mDomain = domain;
    }

    public UserCredentials getOrCreateUserCredentials() {
        if (userCredentialsExist()) {
            return getCredentialsFromSharedPreferences();
        } else {
            final String email = generateEmail(mDomain);
            final String password = generatePassword();
            final UserCredentials userCredentials = new UserCredentials(email, password);

            storeUserCredentials(userCredentials);

            return userCredentials;
        }
    }

    public boolean userCredentialsExist() {
        return mSharedPreferences.contains(USER_KEY_EMAIL) && mSharedPreferences.contains(
                USER_KEY_PASSWORD);
    }

    private String generateEmail(@NonNull final String domain) {
        return UUID.randomUUID().toString() + "@" + domain;
    }

    private String generatePassword() {
        return UUID.randomUUID().toString();
    }

    private UserCredentials getCredentialsFromSharedPreferences() {
        final String email = mSharedPreferences.getString(USER_KEY_EMAIL, "");
        final String password = mSharedPreferences.getString(USER_KEY_PASSWORD, "");
        return new UserCredentials(email, password);
    }

    @SuppressLint("ApplySharedPref")
    private void storeUserCredentials(UserCredentials userCredentials) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_KEY_EMAIL, userCredentials.getEmail());
        editor.putString(USER_KEY_PASSWORD, userCredentials.getPassword());

        editor.commit();
    }

}
