package net.gini.tariffsdk.network.interceptors;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.BuildConfig;
import net.gini.tariffsdk.authentication.AuthenticationService;
import net.gini.tariffsdk.authentication.SessionToken;
import net.gini.tariffsdk.network.Constants;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private final AuthenticationService mAuthenticationService;
    @NonNull
    private final String mClientId;
    @NonNull
    private final String mClientSecret;

    public AuthenticationInterceptor(@NonNull final AuthenticationService authenticationService,
            @NonNull final String clientId, @NonNull final String clientSecret) {
        mAuthenticationService = authenticationService;
        mClientId = clientId;
        mClientSecret = clientSecret;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request authorizedRequest = null;
        String url = originalRequest.url().toString();
        if (url.contains(BuildConfig.BASE_URL)) {
            if (url.contains(Constants.AUTHENTICATE_CLIENT) || url.contains(
                    Constants.AUTHENTICATE_USER)) {
                authorizedRequest = authorizeWithBasic(originalRequest);
            } else {
                authorizedRequest = authorizeWithToken(originalRequest);
            }
        }
        if (authorizedRequest != null) {
            return chain.proceed(authorizedRequest);
        } else {
            return chain.proceed(originalRequest);
        }
    }


    private Request authorizeWithBasic(Request originalRequest) {
        return originalRequest.newBuilder()
                .header(Constants.HEADER_NAME_AUTHORIZATION,
                        Credentials.basic(mClientId, mClientSecret))
                .build();
    }

    private Request authorizeWithToken(Request originalRequest) {
        SessionToken accessToken = AuthenticationService.getSessionToken();
        if (accessToken != null) {
            return originalRequest.newBuilder()
                    .header(Constants.HEADER_NAME_AUTHORIZATION, "BEARER " + accessToken.getToken())
                    .build();
        }
        return null;
    }

}
