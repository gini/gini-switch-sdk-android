package net.gini.switchsdk.authentication;


import net.gini.switchsdk.authentication.models.AccessToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Protocol;
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
            return new Response.Builder()
                    .code(401)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .build();
        }
        return chain.proceed(requestBuilder.build());
    }
}
