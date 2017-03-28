package net.gini.tariffsdk.network.interceptors;


import net.gini.tariffsdk.BuildConfig;
import net.gini.tariffsdk.authentication.AuthenticationService;
import net.gini.tariffsdk.authentication.models.AccessToken;
import net.gini.tariffsdk.network.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(final Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request authorizedRequest = null;
        String url = originalRequest.url().toString();
        if (url.contains(BuildConfig.BASE_URL)) {
            authorizedRequest = authorizeWithToken(originalRequest);
        }
        if (authorizedRequest != null) {
            return chain.proceed(authorizedRequest);
        } else {
            return chain.proceed(originalRequest);
        }
    }

    private Request authorizeWithToken(Request originalRequest) {
        AccessToken accessToken = AuthenticationService.getSessionToken();
        if (accessToken != null) {
            return originalRequest.newBuilder()
                    .header(Constants.HEADER_NAME_AUTHORIZATION, "BEARER " + accessToken.getToken())
                    .build();
        }
        return null;
    }

}
