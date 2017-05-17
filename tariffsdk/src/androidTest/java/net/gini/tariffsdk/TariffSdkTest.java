package net.gini.tariffsdk;

import static junit.framework.Assert.assertEquals;

import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE;
import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_BUTTON_TEXT_COLOR;
import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_EXIT_DIALOG_TEXT;
import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_NEGATIVE_COLOR;
import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_POSITIVE_COLOR;
import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_THEME;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TariffSdkTest {

    private Context mContext;

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetButtonStyle() {
        TariffSdk tariffSdk = TariffSdk.init(mContext, "", "", "");
        tariffSdk.setButtonStyleSelector(12345);
        Intent intent = tariffSdk.getTariffSdkIntent();
        final int buttonTextColor = intent.getIntExtra(BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE, 0);
        assertEquals(12345, buttonTextColor);
    }

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetButtonTextColor() {
        TariffSdk tariffSdk = TariffSdk.init(mContext, "", "", "");
        tariffSdk.setButtonTextColor(12345);
        Intent intent = tariffSdk.getTariffSdkIntent();
        final int buttonTextColor = intent.getIntExtra(BUNDLE_EXTRA_BUTTON_TEXT_COLOR, 0);
        assertEquals(12345, buttonTextColor);
    }

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetExitDialogText() {
        TariffSdk tariffSdk = TariffSdk.init(mContext, "", "", "");
        tariffSdk.setExitDialogText(12345);
        Intent intent = tariffSdk.getTariffSdkIntent();
        final int exitDialogText = intent.getIntExtra(BUNDLE_EXTRA_EXIT_DIALOG_TEXT, 0);
        assertEquals(12345, exitDialogText);
    }

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetNegativeColor() {
        TariffSdk tariffSdk = TariffSdk.init(mContext, "", "", "");
        tariffSdk.setNegativeColor(12345);
        Intent intent = tariffSdk.getTariffSdkIntent();
        final int negativeColor = intent.getIntExtra(BUNDLE_EXTRA_NEGATIVE_COLOR, 0);
        assertEquals(12345, negativeColor);
    }

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetPositiveColor() {
        TariffSdk tariffSdk = TariffSdk.init(mContext, "", "", "");
        tariffSdk.setPositiveColor(12345);
        Intent intent = tariffSdk.getTariffSdkIntent();
        final int positiveColor = intent.getIntExtra(BUNDLE_EXTRA_POSITIVE_COLOR, 0);
        assertEquals(12345, positiveColor);
    }

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetTheme() {
        TariffSdk tariffSdk = TariffSdk.init(mContext, "", "", "");
        tariffSdk.setTheme(12345);
        Intent intent = tariffSdk.getTariffSdkIntent();
        final int theme = intent.getIntExtra(BUNDLE_EXTRA_THEME, 0);
        assertEquals(12345, theme);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldDefaultNegativeColor() {
        TariffSdk tariffSdk = TariffSdk.init(mContext, "", "", "");
        Intent intent = tariffSdk.getTariffSdkIntent();
        final int negativeColor = intent.getIntExtra(BUNDLE_EXTRA_NEGATIVE_COLOR, 0);
        assertEquals(R.color.negativeColor, negativeColor);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldDefaultPositiveColor() {
        TariffSdk tariffSdk = TariffSdk.init(mContext, "", "", "");
        Intent intent = tariffSdk.getTariffSdkIntent();
        final int positiveColor = intent.getIntExtra(BUNDLE_EXTRA_POSITIVE_COLOR, 0);
        assertEquals(R.color.positiveColor, positiveColor);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldHaveGiniTheme() {
        TariffSdk tariffSdk = TariffSdk.init(mContext, "", "", "");
        Intent intent = tariffSdk.getTariffSdkIntent();
        final int theme = intent.getIntExtra(BUNDLE_EXTRA_THEME, 0);
        assertEquals(R.style.GiniTheme, theme);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldNotSetButtonStyle() {
        TariffSdk tariffSdk = TariffSdk.init(mContext, "", "", "");
        Intent intent = tariffSdk.getTariffSdkIntent();
        final int buttonStyle = intent.getIntExtra(BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE, 0);
        assertEquals(0, buttonStyle);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldNotSetButtonTextColor() {
        TariffSdk tariffSdk = TariffSdk.init(mContext, "", "", "");
        Intent intent = tariffSdk.getTariffSdkIntent();
        final int buttonTextColor = intent.getIntExtra(BUNDLE_EXTRA_BUTTON_TEXT_COLOR, 0);
        assertEquals(0, buttonTextColor);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldNotSetExitDialogText() {
        TariffSdk tariffSdk = TariffSdk.init(mContext, "", "", "");
        Intent intent = tariffSdk.getTariffSdkIntent();
        final int exitDialogText = intent.getIntExtra(BUNDLE_EXTRA_EXIT_DIALOG_TEXT, 0);
        assertEquals(R.string.exit_dialog_text, exitDialogText);
    }

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    @SmallTest
    public void setupSDK_shouldBeSingleton() {
        TariffSdk tariffSdk = TariffSdk.init(mContext, "", "", "");
        TariffSdk tariffSdk1 = TariffSdk.init(mContext, "", "", "");
        assertEquals(tariffSdk, tariffSdk1);
    }

    @After
    public void tearDown() {
        TariffSdk.mSingleton = null;
    }

}