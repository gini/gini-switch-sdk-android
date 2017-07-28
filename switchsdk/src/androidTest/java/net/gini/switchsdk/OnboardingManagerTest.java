package net.gini.switchsdk;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class OnboardingManagerTest {

    private Context mContext;
    private OnboardingManager mDefaultOnboardingManager;

    @Test
    @SmallTest
    public void onBoarding_shouldReturnFalseWhenHaveNotBeenShown() {
        boolean onBoardingShown = mDefaultOnboardingManager.onBoardingShown();
        assertFalse("Onboarding shown should be false when it wasn't displayed.", onBoardingShown);
    }

    @Test
    @SmallTest
    public void onBoarding_shouldReturnTrueWhenShown() {
        boolean onBoardingShown = mDefaultOnboardingManager.onBoardingShown();
        assertFalse("Onboarding shown should be false when it wasn't displayed.", onBoardingShown);
        mDefaultOnboardingManager.storeOnboardingShown();
        boolean onBoardingShown2 = mDefaultOnboardingManager.onBoardingShown();
        assertTrue("Should return true when it has been shown.", onBoardingShown2);
    }

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();
        final SharedPreferences sharedPreferences = getOnboardingManagerSharedPreferences();
        sharedPreferences.edit().clear().apply();
        mDefaultOnboardingManager = new OnboardingManager(mContext);
    }

    private SharedPreferences getOnboardingManagerSharedPreferences() {
        return mContext.getSharedPreferences(
                OnboardingManager.ONBOARDING_SHARE_PREFERENCES, Context.MODE_PRIVATE);
    }

}