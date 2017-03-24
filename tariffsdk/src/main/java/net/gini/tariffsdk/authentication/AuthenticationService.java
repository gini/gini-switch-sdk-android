package net.gini.tariffsdk.authentication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

public class AuthenticationService {

    //Application context is fine
    @SuppressLint("StaticFieldLeak")
    private static AuthenticationService mInstance = null;
    private static SessionToken mSessionToken = null;
    private final Context mContext;

    private AuthenticationService(final Context context, final String clientId,
            final String clientPw) {

        mContext = context;
        new RequestToken().execute(new Pair<>(clientId, clientPw));

    }

    public static AuthenticationService getInstance(@NonNull final Context context, @NonNull final String clientId, @NonNull final String clientPw) {
        if (mInstance == null) {
            mInstance = new AuthenticationService(context.getApplicationContext(), clientId, clientPw);
        }
        return mInstance;
    }

    @Nullable
    public static SessionToken getSessionToken() {
        return mSessionToken;
    }

    private static class RequestToken extends AsyncTask<Pair<String, String>, Void, SessionToken> {

        @SafeVarargs
        @Override
        protected final SessionToken doInBackground(final Pair<String, String>... pairs) {
            //TODO network call for requesting the token
            return new SessionToken("session token from: " + pairs[0].first + " " +  pairs[0].second);
        }

        @Override
        protected void onPostExecute(final SessionToken sessionToken) {
            mSessionToken = sessionToken;
        }
    }
}
