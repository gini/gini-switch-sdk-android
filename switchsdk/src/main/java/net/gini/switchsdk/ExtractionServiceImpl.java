package net.gini.switchsdk;


import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import net.gini.switchsdk.network.Extractions;
import net.gini.switchsdk.network.NetworkCallback;
import net.gini.switchsdk.network.SwitchApi;
import net.gini.switchsdk.utils.Logging;

class ExtractionServiceImpl implements ExtractionService {

    private final SwitchApi mSwitchApi;
    private Extractions mExtractionsFromApi;

    ExtractionServiceImpl(SwitchApi switchApi) {
        mSwitchApi = switchApi;
    }

    @Override
    public boolean extractionsAvailable() {
        return mExtractionsFromApi != null;
    }

    @Override
    public void fetchExtractions(@NonNull final String extractionsUrl,
            @NonNull final ExtractionListener listener) {
        mSwitchApi.retrieveExtractions(extractionsUrl, new NetworkCallback<Extractions>() {
            @Override
            public void onError(final Exception e) {
                //TODO
                Logging.e("Fetching extractions from API failed.", e);
            }

            @Override
            public void onSuccess(final Extractions extractions) {
                mExtractionsFromApi = extractions;
                listener.onExtractionsReceived();
            }
        });
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public Extractions getExtractions() {
        return mExtractionsFromApi;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public int getResultCodeForActivity() {
        return SwitchSdk.EXTRACTIONS_AVAILABLE;
    }

}
