package net.gini.switchsdk.smoketests;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import net.gini.switchsdk.OnboardingManager;
import net.gini.switchsdk.R;
import net.gini.switchsdk.SwitchSdk;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SmokeTest {

    @Rule
    public ActivityTestRule<SmokeTestHostActivity> mActivityTestRule = new ActivityTestRule
            <>(SmokeTestHostActivity.class, true, false);
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(
            Manifest.permission.CAMERA);
    private Context mTargetContext;

    @Test
    public void onboardingIsShown() {

        SwitchSdk switchSdk = SwitchSdk.init(mTargetContext, "", "", "");
        Intent intent = new Intent(mTargetContext, SmokeTestHostActivity.class);

        intent.putExtras(switchSdk.getSwitchSdkIntent());
        mActivityTestRule.launchActivity(intent);

        SystemClock.sleep(3000); //TODO verify that animations on test device has been disabled
        onView(withId(R.id.onBoardingContainer)).perform(swipeLeft()).check(matches(isDisplayed()));
        onView(withId(R.id.onBoardingContainer)).perform(swipeLeft()).check(matches(isDisplayed()));
        onView(withId(R.id.onBoardingContainer)).perform(swipeLeft()).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

    }

    @Before
    public void setUp() throws Exception {
        mTargetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final SharedPreferences sharedPreferences = mTargetContext.getSharedPreferences(
                OnboardingManager.ONBOARDING_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

}
