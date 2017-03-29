package net.gini.tariffsdk.authentication.models;


import android.support.annotation.NonNull;

public class UserCredentials {

    private final String mEmail;

    private final String mPassword;

    public UserCredentials(@NonNull final String userEmail, @NonNull final String userPassword) {
        mEmail = userEmail;
        mPassword = userPassword;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }
}
