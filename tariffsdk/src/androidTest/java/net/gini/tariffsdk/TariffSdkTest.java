package net.gini.tariffsdk;

import static junit.framework.Assert.assertEquals;

import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE;
import static net.gini.tariffsdk.TariffSdkBaseActivity.BUNDLE_EXTRA_BUTTON_TEXT_COLOR;
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
    private TariffSdk mTariffSdk;

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetButtonStyle() {
        mTariffSdk.setButtonStyleSelector(12345);
        Intent intent = mTariffSdk.getTariffSdkIntent();
        final int buttonTextColor = intent.getIntExtra(BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE, 0);
        assertEquals(12345, buttonTextColor);
    }

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetButtonTextColor() {
        mTariffSdk.setButtonTextColor(12345);
        Intent intent = mTariffSdk.getTariffSdkIntent();
        final int buttonTextColor = intent.getIntExtra(BUNDLE_EXTRA_BUTTON_TEXT_COLOR, 0);
        assertEquals(12345, buttonTextColor);
    }

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetNegativeColor() {
        mTariffSdk.setNegativeColor(12345);
        Intent intent = mTariffSdk.getTariffSdkIntent();
        final int negativeColor = intent.getIntExtra(BUNDLE_EXTRA_NEGATIVE_COLOR, 0);
        assertEquals(12345, negativeColor);
    }

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetPositiveColor() {
        mTariffSdk.setPositiveColor(12345);
        Intent intent = mTariffSdk.getTariffSdkIntent();
        final int positiveColor = intent.getIntExtra(BUNDLE_EXTRA_POSITIVE_COLOR, 0);
        assertEquals(12345, positiveColor);
    }

    @Test
    @SmallTest
    public void customSettingsSDK_shouldSetTheme() {
        mTariffSdk.setTheme(12345);
        Intent intent = mTariffSdk.getTariffSdkIntent();
        final int theme = intent.getIntExtra(BUNDLE_EXTRA_THEME, 0);
        assertEquals(12345, theme);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldDefaultNegativeColor() {
        Intent intent = mTariffSdk.getTariffSdkIntent();
        final int negativeColor = intent.getIntExtra(BUNDLE_EXTRA_NEGATIVE_COLOR, 0);
        assertEquals(R.color.negativeColor, negativeColor);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldDefaultPositiveColor() {
        Intent intent = mTariffSdk.getTariffSdkIntent();
        final int positiveColor = intent.getIntExtra(BUNDLE_EXTRA_POSITIVE_COLOR, 0);
        assertEquals(R.color.positiveColor, positiveColor);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldHaveGiniTheme() {
        Intent intent = mTariffSdk.getTariffSdkIntent();
        final int theme = intent.getIntExtra(BUNDLE_EXTRA_THEME, 0);
        assertEquals(R.style.GiniTheme, theme);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldNotSetButtonStyle() {
        Intent intent = mTariffSdk.getTariffSdkIntent();
        final int buttonStyle = intent.getIntExtra(BUNDLE_EXTRA_BUTTON_SELECTOR_STYLE, 0);
        assertEquals(0, buttonStyle);
    }

    @Test
    @SmallTest
    public void defaultSettingsSDK_shouldNotSetButtonTextColor() {
        Intent intent = mTariffSdk.getTariffSdkIntent();
        final int buttonTextColor = intent.getIntExtra(BUNDLE_EXTRA_BUTTON_TEXT_COLOR, 0);
        assertEquals(0, buttonTextColor);
    }

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();
        mTariffSdk = TariffSdk.init(mContext, "", "", "");
    }

    @Test
    @SmallTest
    public void setupSDK_shouldBeSingleton() {
        TariffSdk tariffSdk1 = TariffSdk.init(mContext, "", "", "");
        assertEquals(mTariffSdk, tariffSdk1);
    }

    @After
    public void tearDown() {
        TariffSdk.mSingleton = null;
    }

}