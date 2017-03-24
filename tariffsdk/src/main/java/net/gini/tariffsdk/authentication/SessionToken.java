package net.gini.tariffsdk.authentication;


public class SessionToken {

    private final String mToken;

    public SessionToken(final String token) {
        mToken = token;
    }

    public String getToken() {
        return mToken;
    }
}
