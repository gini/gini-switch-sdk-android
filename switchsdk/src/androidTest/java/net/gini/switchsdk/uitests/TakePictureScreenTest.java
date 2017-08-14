package net.gini.switchsdk.uitests;


import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static net.gini.switchsdk.uitests.ScreenUtils.childAtPosition;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.gini.switchsdk.OnboardingManager;
import net.gini.switchsdk.ReviewPictureActivity;
import net.gini.switchsdk.SwitchSdk;
import net.gini.switchsdk.test.R;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TakePictureScreenTest {

    private static final int TIME_TO_WAIT_BETWEEN_STEPS = 1000;
    private static final int TIME_TO_WAIT_FOR_ACTIVITY_TO_START = 4000;
    @Rule
    public IntentsTestRule<HostActivity> mActivityTestRule = new IntentsTestRule
            <>(HostActivity.class, true, false);
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(
            Manifest.permission.CAMERA);
    private Context mTargetContext;

    @Test
    public void cameraScreen_rejectedPictureShouldNotAppearInDocumentBar() {

        omitOnboarding();
        startIntent();

        SystemClock.sleep(TIME_TO_WAIT_BETWEEN_STEPS);
        onView(withId(R.id.button_take_picture))
                .perform(click());

        SystemClock.sleep(TIME_TO_WAIT_FOR_ACTIVITY_TO_START);

        onView(withId(R.id.button_discard)).perform(click());

        SystemClock.sleep(1000);

        onView(withId(R.id.image_overview)).check(new RecyclerViewItemCountAssertion(1));

    }

    @Test
    public void cameraScreen_takePictureShouldOpenReviewScreen() {

        omitOnboarding();
        startIntent();

        SystemClock.sleep(TIME_TO_WAIT_BETWEEN_STEPS);
        onView(withId(R.id.button_take_picture))
                .perform(click());

        SystemClock.sleep(TIME_TO_WAIT_FOR_ACTIVITY_TO_START);
        intended(hasComponent(ReviewPictureActivity.class.getName()));
    }

    @Test
    public void firstLaunch_shouldShowOnboarding() {

        startIntent();

        SystemClock.sleep(3000); //TODO verify that animations on test device has been disabled
        onView(withId(R.id.onBoardingContainer)).perform(swipeLeft()).check(matches(isDisplayed()));
        onView(withId(R.id.onBoardingContainer)).perform(swipeLeft()).check(matches(isDisplayed()));
        onView(withId(R.id.onBoardingContainer)).perform(swipeLeft()).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

    }

    @Test
    public void menu_cancelShouldDisplayDialog() {

        omitOnboarding();

        startIntent();

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText(R.string.menu_entry_cancel), isDisplayed()));
        appCompatTextView.perform(click());

        SystemClock.sleep(TIME_TO_WAIT_BETWEEN_STEPS);

        onView(withText(R.string.exit_dialog_text))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

        onView(withId(android.R.id.button1))
                .inRoot(isDialog())
                .check(matches(withText(R.string.confirm)))
                .check(matches(isDisplayed()));

        onView(withId(android.R.id.button2))
                .inRoot(isDialog())
                .check(matches(withText(R.string.cancel)))
                .check(matches(isDisplayed()));


    }

    @Test
    public void menu_helpShouldDisplayOnboarding() {

        omitOnboarding();
        startIntent();

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText(R.string.menu_entry_help), isDisplayed()));
        appCompatTextView.perform(click());

        SystemClock.sleep(TIME_TO_WAIT_BETWEEN_STEPS);

        onView(withId(R.id.onBoardingViewPager)).check(matches(isDisplayed()));

    }

    @Test
    public void menu_shouldContainAllItems() {

        omitOnboarding();
        startIntent();

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction textView = onView(
                allOf(withId(R.id.title), withText(R.string.menu_entry_cancel),
                        childAtPosition(childAtPosition(
                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                0), 0),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.title), withText(R.string.menu_entry_help),
                        childAtPosition(childAtPosition(
                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                0), 0),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

    }

    @Before
    public void setUp() throws Exception {
        mTargetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final SharedPreferences sharedPreferences = mTargetContext.getSharedPreferences(
                OnboardingManager.ONBOARDING_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    @After
    public void tearDown() {
        SwitchSdk.getSdk().cleanUp();
    }

    private void omitOnboarding() {
        final SharedPreferences sharedPreferences = mTargetContext.getSharedPreferences(
                OnboardingManager.ONBOARDING_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(OnboardingManager.ONBOARDING_KEY_SHOWN, true).apply();
    }

    private void startIntent() {
        SwitchSdk switchSdk = SwitchSdk.init(mTargetContext, "", "", "");
        Intent intent = new Intent(mTargetContext, HostActivity.class);

        intent.putExtras(switchSdk.getSwitchSdkIntent());
        mActivityTestRule.launchActivity(intent);
    }

    private class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }

}
