package net.gini.tariffsdk.network.interceptors;


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
        final Request originalRequest = chain.request();
        final Request authorizedRequest = authorizeWithBasic(originalRequest);

        return chain.proceed(authorizedRequest);

    }

    private Request authorizeWithBasic(Request originalRequest) {
        return originalRequest.newBuilder()
                .header(Constants.HEADER_NAME_AUTHORIZATION,
                        Credentials.basic(mClientCredentials.getClientId(),
                                mClientCredentials.getClientSecret())).build();
    }

}
