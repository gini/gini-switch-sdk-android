package net.gini.switchsdk.uitests;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static junit.framework.Assert.assertEquals;

import static net.gini.switchsdk.uitests.ScreenUtils.childAtPosition;

import static org.hamcrest.Matchers.allOf;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.view.View;

import net.gini.switchsdk.OnboardingManager;
import net.gini.switchsdk.R;
import net.gini.switchsdk.ReviewPictureActivity;
import net.gini.switchsdk.SwitchSdk;
import net.gini.switchsdk.TakePictureActivity;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Iterator;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ReviewPictureScreenTest extends BaseUITests {

    @Rule
    public ActivityTestRule<HostActivity> mActivityTestRule = new ActivityTestRule
            <HostActivity>(HostActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context context = getInstrumentation().getTargetContext();
            SwitchSdk switchSdk = SwitchSdk.init(context, "", "", "");
            Intent intent = new Intent(context, HostActivity.class);

            intent.putExtras(switchSdk.getSwitchSdkIntent());
            return intent;
        }
    };
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(
            Manifest.permission.CAMERA);

    @Test
    public void menu_cancelShouldDisplayDialog() {
        super.menu_cancelShouldDisplayDialog();
    }

    @Test
    public void menu_helpShouldDisplayOnboarding() {
        super.menu_helpShouldDisplayOnboarding();

    }

    @Test
    public void menu_shouldContainAllItems() {
        super.menu_shouldContainAllItems();
    }

    @Test
    public void backButton_shouldFinishReviewScreen() {
        intended(hasComponent(ReviewPictureActivity.class.getName()));
        Espresso.pressBack();
        SystemClock.sleep(TIME_TO_WAIT_BETWEEN_STEPS_FAST);
        assertEquals(getActivityInstance().getLocalClassName(),
                TakePictureActivity.class.getName());
    }

    @Test
    public void reviewScreen_shouldContainAllElements() {
        onView(withId(R.id.button_discard)).check(matches(isDisplayed()));
        onView(withId(R.id.button_keep)).check(matches(isDisplayed()));
        onView(withId(R.id.button_rotate)).check(matches(isDisplayed()));
        onView(withId(R.id.image_preview_container)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.title), withText(R.string.review_screen_title), childAtPosition(
                allOf(withId(R.id.toolbar), childAtPosition(
                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class), 0)), 0),
                isDisplayed()))
                .check(matches(isDisplayed()));
    }

    @Before
    public void setUp() throws Exception {
        Intents.init();
        Context context = getInstrumentation().getTargetContext();
        final SharedPreferences sharedPreferences = context.getSharedPreferences(
                OnboardingManager.ONBOARDING_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(OnboardingManager.ONBOARDING_KEY_SHOWN, true).apply();

        startReviewScreen();
        SystemClock.sleep(3000);
    }

    @After
    public void tearDown() {
        Intents.release();
        SwitchSdk.getSdk().cleanUp();
    }

    private Activity getActivityInstance() {
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivity =
                        ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(
                                Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                currentActivity[0] = it.next();
            }
        });

        return currentActivity[0];
    }

    private void startReviewScreen() {
        onView(withId(R.id.button_take_picture)).perform(click());
    }
}
