package net.gini.switchsdk;


import static android.support.annotation.VisibleForTesting.PACKAGE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.VisibleForTesting;

@VisibleForTesting(otherwise = PACKAGE_PRIVATE)
public class OnboardingManager {

    @VisibleForTesting
    public static final String ONBOARDING_KEY_SHOWN = "ONBOARDING_KEY_SHOWN";
    @VisibleForTesting
    public static final String ONBOARDING_SHARE_PREFERENCES = "ONBOARDING_SHARE_PREFERENCES";
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
