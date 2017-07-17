package net.gini.switchsdk.authentication;


import net.gini.switchsdk.authentication.models.AccessToken;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class BearerAuthenticator implements Authenticator {

    private final AuthenticationService mAuthenticationService;

    public BearerAuthenticator(final AuthenticationService authenticationService) {
        mAuthenticationService = authenticationService;
    }

    @Override
    public Request authenticate(final Route route, final Response response) throws IOException {

        if (responseCount(response) >= 3) {
            return null;
        }

        Request.Builder builder = response.request().newBuilder();
        builder.removeHeader("Authorization");

        final AccessToken token = mAuthenticationService.requestNewUserToken();

        return builder
                .addHeader("Authorization", "BEARER " + token.getToken())
                .build();
    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
}
