package net.gini.tariffsdk.authentication.models;

public class ClientCredentials {

    private final String mClientId;
    private final String mClientSecret;

    public ClientCredentials(String clientId, String clientSecret) {
        mClientId = clientId;
        mClientSecret = clientSecret;
    }

    public String getClientId() {
        return mClientId;
    }

    public String getClientSecret() {
        return mClientSecret;
    }
}
