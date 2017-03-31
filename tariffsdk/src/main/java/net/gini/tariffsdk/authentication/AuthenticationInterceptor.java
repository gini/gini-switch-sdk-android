package net.gini.tariffsdk.authentication;


import net.gini.tariffsdk.authentication.models.AccessToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private final AuthenticationService mAuthenticationService;

    public AuthenticationInterceptor(final AuthenticationService authenticationService) {
        mAuthenticationService = authenticationService;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        final Request.Builder requestBuilder = chain.request().newBuilder();

        AccessToken userToken = mAuthenticationService.getUserToken();
        if (userToken != null) {
            requestBuilder.addHeader("Authorization", "BEARER " + userToken.getToken());
        } else {
            throw new IOException("TODO");
        }
        return chain.proceed(requestBuilder.build());
    }
}
