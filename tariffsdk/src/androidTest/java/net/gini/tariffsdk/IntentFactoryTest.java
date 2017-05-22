package net.gini.tariffsdk;

import static junit.framework.Assert.assertEquals;

import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_BUTTON_TEXT;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_EDIT_TEXT_BACKGROUND_COLOR;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_EDIT_TEXT_COLOR;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_HINT_COLOR;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_LINE_COLOR;

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
public class IntentFactoryTest {

    private Context mContext;
    private TariffSdk mTariffSdk;

    @Test
    @SmallTest
    public void custom_shouldHaveCustomButtonText() {
        mTariffSdk.setExtractionButtonText(12345);
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int buttonText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_TEXT, 0);
        assertEquals(12345, buttonText);
    }

    @Test
    @SmallTest
    public void custom_shouldHaveCustomEditTextBackgroundColor() {
        mTariffSdk.setExtractionEditTextBackgroundColor(12345);
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_EDIT_TEXT_BACKGROUND_COLOR, 0);
        assertEquals(12345, textColor);
    }

    @Test
    @SmallTest
    public void custom_shouldHaveCustomEditTextColor() {
        mTariffSdk.setExtractionEditTextColor(12345);
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_EDIT_TEXT_COLOR, 0);
        assertEquals(12345, textColor);
    }

    @Test
    @SmallTest
    public void custom_shouldHaveCustomHintTextColor() {
        mTariffSdk.setExtractionHintColor(12345);
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_HINT_COLOR, 0);
        assertEquals(12345, textColor);
    }

    @Test
    @SmallTest
    public void custom_shouldHaveCustomImage() {
        mTariffSdk.setAnalyzedImage(12345);
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int image = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE, 0);
        assertEquals(12345, image);
    }

    @Test
    @SmallTest
    public void custom_shouldHaveCustomLineColor() {
        mTariffSdk.setExtractionLineColor(12345);
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_LINE_COLOR, 0);
        assertEquals(12345, textColor);
    }

    @Test
    @SmallTest
    public void custom_shouldHaveCustomText() {
        mTariffSdk.setAnalyzedText(12345);
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int analyzedText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT, 0);
        assertEquals(12345, analyzedText);
    }

    @Test
    @SmallTest
    public void custom_shouldHaveCustomTextColor() {
        mTariffSdk.setAnalyzedTextColor(12345);
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int color = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR, 0);
        assertEquals(12345, color);
    }

    @Test
    @SmallTest
    public void custom_shouldHaveCustomTextSize() {
        mTariffSdk.setAnalyzedTextSize(16);
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int textSize = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE, 0);
        assertEquals(16, textSize);
    }

    @Test
    @SmallTest
    public void default_shouldHaveDefaultButtonText() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_TEXT, 0);
        assertEquals(R.string.button_extractions, textColor);
    }

    @Test
    @SmallTest
    public void default_shouldHaveDefaultEditTextBackgroundColor() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_EDIT_TEXT_BACKGROUND_COLOR, 0);
        assertEquals(R.color.secondaryColor, textColor);
    }

    @Test
    @SmallTest
    public void default_shouldHaveDefaultEditTextColor() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_EDIT_TEXT_COLOR, 0);
        assertEquals(R.color.primaryText, textColor);
    }

    @Test
    @SmallTest
    public void default_shouldHaveDefaultHintTextColor() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_HINT_COLOR, 0);
        assertEquals(R.color.secondaryText, textColor);
    }

    @Test
    @SmallTest
    public void default_shouldHaveDefaultImage() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int image = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE, 0);
        assertEquals(R.drawable.ic_check_circle, image);
    }

    @Test
    @SmallTest
    public void default_shouldHaveDefaultLineColor() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_LINE_COLOR, 0);
        assertEquals(R.color.primaryText, textColor);
    }

    @Test
    @SmallTest
    public void default_shouldHaveDefaultText() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int analyzedText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT, 0);
        assertEquals(R.string.analyzed_text, analyzedText);
    }

    @Test
    @SmallTest
    public void default_shouldHaveDefaultTextColor() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int color = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR, 0);
        assertEquals(R.color.primaryText, color);
    }

    @Test
    @SmallTest
    public void default_shouldHaveDefaultTextSize() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int textSize = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE, 0);
        int defaultSize = mContext.getResources().getInteger(R.integer.analyzed_text_size);
        assertEquals(defaultSize, textSize);
    }

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();
        mTariffSdk = TariffSdk.init(mContext, "", "", "");
    }

    @After
    public void tearDown() {
        TariffSdk.mSingleton = null;
    }
}