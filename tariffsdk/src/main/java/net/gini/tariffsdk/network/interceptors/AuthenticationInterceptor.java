package net.gini.tariffsdk.network.interceptors;


import net.gini.tariffsdk.BuildConfig;
import net.gini.tariffsdk.authentication.models.ClientCredentials;
import net.gini.tariffsdk.network.Constants;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private final ClientCredentials mClientCredentials;

    public AuthenticationInterceptor(final ClientCredentials clientCredentials) {

        mClientCredentials = clientCredentials;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request authorizedRequest = null;
        String url = originalRequest.url().toString();
        if (url.contains(BuildConfig.BASE_URL)) {
            authorizedRequest = authorizeWithBasic(originalRequest);
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
                        Credentials.basic(mClientCredentials.getClientId(),
                                mClientCredentials.getClientSecret())).build();
    }

}
