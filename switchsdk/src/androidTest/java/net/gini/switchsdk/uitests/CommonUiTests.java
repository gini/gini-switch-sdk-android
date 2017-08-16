package net.gini.switchsdk.uitests;


import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static net.gini.switchsdk.uitests.ScreenUtils.childAtPosition;

import static org.hamcrest.Matchers.allOf;

import android.os.SystemClock;
import android.support.test.espresso.ViewInteraction;
import android.view.View;

import net.gini.switchsdk.R;

import org.hamcrest.core.IsInstanceOf;

class CommonUiTests {
    static final int TIME_TO_WAIT_BETWEEN_STEPS_FAST = 1000;
    static final int TIME_TO_WAIT_BETWEEN_STEPS_MEDIUM = 3000;
    static final int TIME_TO_WAIT_BETWEEN_STEPS_SLOW = 4000;


    protected void menu_cancelShouldDisplayDialog() {

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        SystemClock.sleep(TIME_TO_WAIT_BETWEEN_STEPS_FAST);
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText(R.string.menu_entry_cancel), isDisplayed()));
        appCompatTextView.perform(click());

        SystemClock.sleep(TIME_TO_WAIT_BETWEEN_STEPS_FAST);

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

    protected void menu_helpShouldDisplayOnboarding() {

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        SystemClock.sleep(TIME_TO_WAIT_BETWEEN_STEPS_FAST);
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText(R.string.menu_entry_help), isDisplayed()));
        appCompatTextView.perform(click());

        SystemClock.sleep(TIME_TO_WAIT_BETWEEN_STEPS_FAST);

        onView(withId(R.id.onBoardingViewPager)).check(matches(isDisplayed()));

    }

    protected void menu_shouldContainAllItems() {

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        SystemClock.sleep(TIME_TO_WAIT_BETWEEN_STEPS_FAST);
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
}
