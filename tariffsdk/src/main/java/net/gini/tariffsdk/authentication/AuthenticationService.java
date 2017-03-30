package net.gini.tariffsdk.authentication;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.network.NetworkCallback;

public interface AuthenticationService {

    void init(@NonNull final NetworkCallback<Void> callback);
}
