package net.gini.tariffsdk.network;


import android.support.annotation.NonNull;

import net.gini.tariffsdk.remoteconfig.RemoteValues;

public interface RemoteConfigApi {

    void requestRemoteConfig(@NonNull final NetworkCallback<RemoteValues> callback);
}
