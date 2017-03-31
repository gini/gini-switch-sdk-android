package net.gini.tariffsdk.authentication;


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

        requestBuilder.addHeader("Authorization", "BEARER: " + mAuthenticationService.getUserToken());
        return chain.proceed(requestBuilder.build());
    }
}
