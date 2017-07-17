package net.gini.switchsdk.authentication;


public class SessionToken {

    private final String mToken;

    public SessionToken(final String token) {
        mToken = token;
    }

    public String getToken() {
        return mToken;
    }
}
