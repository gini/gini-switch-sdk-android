package net.gini.tariffsdk.network;


public interface Constants {
    String AUTHENTICATE_CLIENT = "oauth/token?grant_type=client_credentials";

    String AUTHENTICATE_USER = "oauth/token?grant_type=password";

    String HEADER_NAME_AUTHORIZATION = "Authorization";
}
