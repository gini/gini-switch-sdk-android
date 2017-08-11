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
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import net.gini.switchsdk.OnboardingManager;
import net.gini.switchsdk.R;
import net.gini.switchsdk.ReviewPictureActivity;
import net.gini.switchsdk.SwitchSdk;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TakePictureScreenTest {

    @Rule
    public IntentsTestRule<HostActivity> mActivityTestRule = new IntentsTestRule
            <>(HostActivity.class, true, false);
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(
            Manifest.permission.CAMERA);
    private Context mTargetContext;

    @Test
    public void cameraScreen_cancelShouldDisplayDialog() {

        final SharedPreferences sharedPreferences = mTargetContext.getSharedPreferences(
                OnboardingManager.ONBOARDING_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(OnboardingManager.ONBOARDING_KEY_SHOWN, true).apply();

        SwitchSdk switchSdk = SwitchSdk.init(mTargetContext, "", "", "");
        Intent intent = new Intent(mTargetContext, HostActivity.class);

        intent.putExtras(switchSdk.getSwitchSdkIntent());
        mActivityTestRule.launchActivity(intent);

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Abbrechen"), isDisplayed()));
        appCompatTextView.perform(click());

        SystemClock.sleep(1000);

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
    public void cameraScreen_clickOnEmptyItemShouldShowCameraPreview() {

        final SharedPreferences sharedPreferences = mTargetContext.getSharedPreferences(
                OnboardingManager.ONBOARDING_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(OnboardingManager.ONBOARDING_KEY_SHOWN, true).apply();

        SwitchSdk switchSdk = SwitchSdk.init(mTargetContext, "", "", "");
        Intent intent = new Intent(mTargetContext, HostActivity.class);

        intent.putExtras(switchSdk.getSwitchSdkIntent());
        mActivityTestRule.launchActivity(intent);

        SystemClock.sleep(1000);
        onView(withId(R.id.button_take_picture))
                .perform(click());

        SystemClock.sleep(4000);

        onView(withId(R.id.button_keep)).perform(click());

        SystemClock.sleep(1000);

        onView(withId(R.id.image_overview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        SystemClock.sleep(1000);

        ViewInteraction imageView = onView(allOf(withId(R.id.image_review), childAtPosition(
                allOf(withId(R.id.image_review), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 1)), 0),
                isDisplayed()));
        imageView.check(matches(isDisplayed()));

        onView(withId(R.id.image_overview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        SystemClock.sleep(1000);

        onView(withId(R.id.camera_frame)).check(matches(isDisplayed()));
        onView(withId(R.id.container_take_picture_buttons)).check(matches(isDisplayed()));
        onView(withId(R.id.button_finish)).check(matches(isDisplayed()));
        onView(withId(R.id.container_take_picture_buttons)).check(matches(isDisplayed()));

    }

    @Test
    public void cameraScreen_clickOnPictureShouldShowIt() {

        final SharedPreferences sharedPreferences = mTargetContext.getSharedPreferences(
                OnboardingManager.ONBOARDING_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(OnboardingManager.ONBOARDING_KEY_SHOWN, true).apply();

        SwitchSdk switchSdk = SwitchSdk.init(mTargetContext, "", "", "");
        Intent intent = new Intent(mTargetContext, HostActivity.class);

        intent.putExtras(switchSdk.getSwitchSdkIntent());
        mActivityTestRule.launchActivity(intent);

        SystemClock.sleep(1000);
        onView(withId(R.id.button_take_picture))
                .perform(click());

        SystemClock.sleep(4000);

        onView(withId(R.id.button_keep)).perform(click());

        SystemClock.sleep(1000);

        onView(withId(R.id.image_overview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        SystemClock.sleep(1000);

        ViewInteraction imageView = onView(allOf(withId(R.id.image_review), childAtPosition(
                allOf(withId(R.id.image_review), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 1)), 0),
                isDisplayed()));
        imageView.check(matches(isDisplayed()));

        onView(withId(R.id.container_preview_buttons)).check(matches(isDisplayed()));
        onView(withId(R.id.button_delete_image)).check(matches(isDisplayed()));
        onView(withId(R.id.button_take_new_image)).check(matches(isDisplayed()));

    }

    @Test
    public void cameraScreen_helpShouldDisplayOnboarding() {

        final SharedPreferences sharedPreferences = mTargetContext.getSharedPreferences(
                OnboardingManager.ONBOARDING_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(OnboardingManager.ONBOARDING_KEY_SHOWN, true).apply();

        SwitchSdk switchSdk = SwitchSdk.init(mTargetContext, "", "", "");
        Intent intent = new Intent(mTargetContext, HostActivity.class);

        intent.putExtras(switchSdk.getSwitchSdkIntent());
        mActivityTestRule.launchActivity(intent);

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Hilfe"), isDisplayed()));
        appCompatTextView.perform(click());

        SystemClock.sleep(1000);

        onView(withId(R.id.onBoardingContainer)).check(matches(isDisplayed()));

    }

    @Test
    public void cameraScreen_rejectedPictureShouldNotAppearInDocumentBar() {

        final SharedPreferences sharedPreferences = mTargetContext.getSharedPreferences(
                OnboardingManager.ONBOARDING_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(OnboardingManager.ONBOARDING_KEY_SHOWN, true).apply();

        SwitchSdk switchSdk = SwitchSdk.init(mTargetContext, "", "", "");
        Intent intent = new Intent(mTargetContext, HostActivity.class);

        intent.putExtras(switchSdk.getSwitchSdkIntent());
        mActivityTestRule.launchActivity(intent);

        SystemClock.sleep(1000);
        onView(withId(R.id.button_take_picture))
                .perform(click());

        SystemClock.sleep(4000);

        onView(withId(R.id.button_discard)).perform(click());

        SystemClock.sleep(1000);

        onView(withId(R.id.image_overview)).check(new RecyclerViewItemCountAssertion(1));

    }

    @Test
    public void cameraScreen_shouldContainAllMenus() {

        final SharedPreferences sharedPreferences = mTargetContext.getSharedPreferences(
                OnboardingManager.ONBOARDING_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(OnboardingManager.ONBOARDING_KEY_SHOWN, true).apply();

        SwitchSdk switchSdk = SwitchSdk.init(mTargetContext, "", "", "");
        Intent intent = new Intent(mTargetContext, HostActivity.class);

        intent.putExtras(switchSdk.getSwitchSdkIntent());
        mActivityTestRule.launchActivity(intent);

        ViewInteraction imageView2 = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(childAtPosition(withId(R.id.toolbar), 2), 0),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction textView = onView(
                allOf(withId(R.id.title), withText("Abbrechen"), childAtPosition(childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 0), 0),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.title), withText("Hilfe"),
                        childAtPosition(childAtPosition(
                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                0), 0),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

    }

    @Test
    public void cameraScreen_takePictureShouldOpenReviewScreen() {

        final SharedPreferences sharedPreferences = mTargetContext.getSharedPreferences(
                OnboardingManager.ONBOARDING_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(OnboardingManager.ONBOARDING_KEY_SHOWN, true).apply();

        SwitchSdk switchSdk = SwitchSdk.init(mTargetContext, "", "", "");
        Intent intent = new Intent(mTargetContext, HostActivity.class);

        intent.putExtras(switchSdk.getSwitchSdkIntent());
        mActivityTestRule.launchActivity(intent);

        SystemClock.sleep(1000);
        onView(withId(R.id.button_take_picture))
                .perform(click());

        SystemClock.sleep(5000);
        intended(hasComponent(ReviewPictureActivity.class.getName()));
    }

    @Test
    public void cameraScreen_takenPictureShouldAppearInDocumentBar() {

        final SharedPreferences sharedPreferences = mTargetContext.getSharedPreferences(
                OnboardingManager.ONBOARDING_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(OnboardingManager.ONBOARDING_KEY_SHOWN, true).apply();

        SwitchSdk switchSdk = SwitchSdk.init(mTargetContext, "", "", "");
        Intent intent = new Intent(mTargetContext, HostActivity.class);

        intent.putExtras(switchSdk.getSwitchSdkIntent());
        mActivityTestRule.launchActivity(intent);

        SystemClock.sleep(1000);
        onView(withId(R.id.button_take_picture)).perform(click());

        SystemClock.sleep(4000);

        onView(withId(R.id.button_keep)).perform(click());

        SystemClock.sleep(1000);

        ViewInteraction textView = onView(allOf(withId(R.id.item_label), withText("Foto 1"),
                childAtPosition(childAtPosition(withId(R.id.image_overview), 0), 1),
                isDisplayed()));
        textView.check(matches(withText("Foto 1")));

        onView(withId(R.id.image_overview)).check(new RecyclerViewItemCountAssertion(2));

    }

    @Test
    public void firstLaunch_shouldShowOnboarding() {

        SwitchSdk switchSdk = SwitchSdk.init(mTargetContext, "", "", "");
        Intent intent = new Intent(mTargetContext, HostActivity.class);

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

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
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
