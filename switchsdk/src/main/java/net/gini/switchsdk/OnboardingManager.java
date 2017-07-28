package net.gini.switchsdk;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.VisibleForTesting;

class OnboardingManager {

    @VisibleForTesting
    static final String ONBOARDING_KEY_SHOWN = "ONBOARDING_KEY_SHOWN";
    @VisibleForTesting
    static final String ONBOARDING_SHARE_PREFERENCES = "ONBOARDING_SHARE_PREFERENCES";

    private final Context mContext;

    public OnboardingManager(final Context context) {
        mContext = context;
    }

    boolean onBoardingShown() {
        return getPreferences().getBoolean(ONBOARDING_KEY_SHOWN, false);
    }

    void storeOnboardingShown() {
        getPreferences().edit().putBoolean(ONBOARDING_KEY_SHOWN, true).apply();
    }

    private SharedPreferences getPreferences() {
        return mContext.getSharedPreferences(ONBOARDING_SHARE_PREFERENCES, Context.MODE_PRIVATE);
    }
}
