package net.gini.tariffsdk;

import static junit.framework.Assert.assertEquals;

import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR;
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE;

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
    public void custom_shouldHaveCustomImage() {
        mTariffSdk.setAnalyzedImage(12345);
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int image = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE, 0);
        assertEquals(12345, image);
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
    public void default_shouldHaveDefaultImage() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int image = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE, 0);
        assertEquals(R.drawable.ic_check_circle, image);
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
        assertEquals(R.color.titleTextColor, color);
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