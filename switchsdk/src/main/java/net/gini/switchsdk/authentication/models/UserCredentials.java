package net.gini.switchsdk.authentication.models;


import android.support.annotation.NonNull;

public class UserCredentials {

    private final String mEmail;

    private final String mPassword;

    public UserCredentials(@NonNull final String userEmail, @NonNull final String userPassword) {
        mEmail = userEmail;
        mPassword = userPassword;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UserCredentials that = (UserCredentials) o;

        return mEmail.equals(that.mEmail) && mPassword.equals(that.mPassword);

    }

    @Override
    public int hashCode() {
        int result = mEmail.hashCode();
        result = 31 * result + mPassword.hashCode();
        return result;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }
}
