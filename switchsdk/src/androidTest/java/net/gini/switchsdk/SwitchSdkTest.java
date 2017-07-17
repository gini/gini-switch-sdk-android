package net.gini.switchsdk;

import static junit.framework.Assert.assertEquals;

import static net.gini.switchsdk.SwitchSdkBaseActivity.BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE;
import static net.gini.switchsdk.SwitchSdkBaseActivity.BUNDLE_EXTRA_BUTTON_TEXT_COLOR;
import static net.gini.switchsdk.SwitchSdkBaseActivity.BUNDLE_EXTRA_EXIT_DIALOG_TEXT;
import static net.gini.switchsdk.SwitchSdkBaseActivity.BUNDLE_EXTRA_NEGATIVE_COLOR;
import static net.gini.switchsdk.SwitchSdkBaseActivity.BUNDLE_EXTRA_POSITIVE_COLOR;
import static net.gini.switchsdk.SwitchSdkBaseActivity.BUNDLE_EXTRA_THEME;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import net.gini.switchsdk.network.SwitchApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class SwitchSdkTest {

    private Context mContext;
    private RemoteConfigManager mMockRemoteConfigManager;
    private SwitchSdk mSwitchSdk;

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetButtonStyle() {
        mSwitchSdk.setButtonStyleSelector(12345);
        Intent intent = mSwitchSdk.getTariffSdkIntent();
        final int buttonTextColor = intent.getIntExtra(BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE, 0);
        assertEquals(12345, buttonTextColor);
    }

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetButtonTextColor() {
        mSwitchSdk.setButtonTextColor(12345);
        Intent intent = mSwitchSdk.getTariffSdkIntent();
        final int buttonTextColor = intent.getIntExtra(BUNDLE_EXTRA_BUTTON_TEXT_COLOR, 0);
        assertEquals(12345, buttonTextColor);
    }

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetExitDialogText() {
        mSwitchSdk.setExitDialogText(12345);
        Intent intent = mSwitchSdk.getTariffSdkIntent();
        final int exitDialogText = intent.getIntExtra(BUNDLE_EXTRA_EXIT_DIALOG_TEXT, 0);
        assertEquals(12345, exitDialogText);
    }

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetNegativeColor() {
        mSwitchSdk.setNegativeColor(12345);
        Intent intent = mSwitchSdk.getTariffSdkIntent();
        final int negativeColor = intent.getIntExtra(BUNDLE_EXTRA_NEGATIVE_COLOR, 0);
        assertEquals(12345, negativeColor);
    }

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetPositiveColor() {
        mSwitchSdk.setPositiveColor(12345);
        Intent intent = mSwitchSdk.getTariffSdkIntent();
        final int positiveColor = intent.getIntExtra(BUNDLE_EXTRA_POSITIVE_COLOR, 0);
        assertEquals(12345, positiveColor);
    }

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetTheme() {
        mSwitchSdk.setTheme(12345);
        Intent intent = mSwitchSdk.getTariffSdkIntent();
        final int theme = intent.getIntExtra(BUNDLE_EXTRA_THEME, 0);
        assertEquals(12345, theme);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldDefaultNegativeColor() {
        Intent intent = mSwitchSdk.getTariffSdkIntent();
        final int negativeColor = intent.getIntExtra(BUNDLE_EXTRA_NEGATIVE_COLOR, 0);
        assertEquals(R.color.negativeColor, negativeColor);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldDefaultPositiveColor() {
        Intent intent = mSwitchSdk.getTariffSdkIntent();
        final int positiveColor = intent.getIntExtra(BUNDLE_EXTRA_POSITIVE_COLOR, 0);
        assertEquals(R.color.positiveColor, positiveColor);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldHaveGiniTheme() {
        Intent intent = mSwitchSdk.getTariffSdkIntent();
        final int theme = intent.getIntExtra(BUNDLE_EXTRA_THEME, 0);
        assertEquals(R.style.GiniTheme, theme);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldNotSetButtonStyle() {
        Intent intent = mSwitchSdk.getTariffSdkIntent();
        final int buttonStyle = intent.getIntExtra(BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE, 0);
        assertEquals(0, buttonStyle);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldNotSetButtonTextColor() {
        Intent intent = mSwitchSdk.getTariffSdkIntent();
        final int buttonTextColor = intent.getIntExtra(BUNDLE_EXTRA_BUTTON_TEXT_COLOR, 0);
        assertEquals(0, buttonTextColor);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldNotSetExitDialogText() {
        Intent intent = mSwitchSdk.getTariffSdkIntent();
        final int exitDialogText = intent.getIntExtra(BUNDLE_EXTRA_EXIT_DIALOG_TEXT, 0);
        assertEquals(R.string.exit_dialog_text, exitDialogText);
    }

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();
        final SwitchApi mockApi = Mockito.mock(SwitchApi.class);
        mSwitchSdk = SwitchSdk.create(mContext, null, null, new RemoteConfigManager(mockApi));
    }

    @Test
    @SmallTest
    public void setupSDK_shouldBeSingleton() {
        SwitchSdk switchSdk1 = SwitchSdk.init(mContext, "", "", "");
        assertEquals(mSwitchSdk, switchSdk1);
    }

    @After
    public void tearDown() {
        SwitchSdk.mSingleton = null;
    }

}