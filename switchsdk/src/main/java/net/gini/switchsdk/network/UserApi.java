package net.gini.switchsdk.network;


import android.support.annotation.NonNull;

import net.gini.switchsdk.authentication.models.AccessToken;
import net.gini.switchsdk.authentication.models.UserCredentials;

import java.io.IOException;

public interface UserApi {

    void createUser(@NonNull final UserCredentials userCredentials,
            @NonNull final AccessToken accessToken, @NonNull final NetworkCallback<Void> callback);

    void requestClientToken(@NonNull final NetworkCallback<AccessToken> callback);

    AccessToken requestNewUserTokenSync(@NonNull UserCredentials userCredentials)
            throws IOException;

    void requestUserToken(@NonNull UserCredentials userCredentials,
            @NonNull final NetworkCallback<AccessToken> callback);

}
