package net.gini.tariffsdk;


import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import net.gini.tariffsdk.network.Extractions;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.TariffApi;

class ExtractionServiceImpl implements ExtractionService {

    private final TariffApi mTariffApi;
    private Extractions mExtractionsFromApi;

    ExtractionServiceImpl(TariffApi tariffApi) {
        mTariffApi = tariffApi;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public Extractions fetchExtractions() {
        return mExtractionsFromApi;
    }

    @Override
    public void fetchExtractions(@NonNull final String extractionsUrl,
            @NonNull final ExtractionListener listener) {
        mTariffApi.retrieveExtractions(extractionsUrl, new NetworkCallback<Extractions>() {
            @Override
            public void onError(final Exception e) {
                //TODO
            }

            @Override
            public void onSuccess(final Extractions extractions) {
                mExtractionsFromApi = extractions;
                listener.onExtractionsReceived();
            }
        });
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public int getResultCodeForActivity() {
        return TariffSdk.EXTRACTIONS_AVAILABLE;
    }

}
