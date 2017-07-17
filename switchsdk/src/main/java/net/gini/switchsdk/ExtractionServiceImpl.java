package net.gini.switchsdk;


import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import net.gini.switchsdk.network.Extractions;
import net.gini.switchsdk.network.NetworkCallback;
import net.gini.switchsdk.network.TariffApi;
import net.gini.switchsdk.utils.Logging;

class ExtractionServiceImpl implements ExtractionService {

    private final TariffApi mTariffApi;
    private Extractions mExtractionsFromApi;

    ExtractionServiceImpl(TariffApi tariffApi) {
        mTariffApi = tariffApi;
    }

    @Override
    public boolean extractionsAvailable() {
        return mExtractionsFromApi != null;
    }

    @Override
    public void fetchExtractions(@NonNull final String extractionsUrl,
            @NonNull final ExtractionListener listener) {
        mTariffApi.retrieveExtractions(extractionsUrl, new NetworkCallback<Extractions>() {
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
        return TariffSdk.EXTRACTIONS_AVAILABLE;
    }

}
