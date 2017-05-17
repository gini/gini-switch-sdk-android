package net.gini.tariffsdk;

import static junit.framework.Assert.assertEquals;

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
    public void custom_shouldHaveCustomText() {
        mTariffSdk.setAnalyzedText(12345);
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int analyzedText = extractionsActivity.getIntExtra("BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT", 0);
        assertEquals(12345, analyzedText);
    }

    @Test
    @SmallTest
    public void default_shouldHaveDefaultText() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        Intent extractionsActivity = intentFactory.createExtractionsActivity();
        int analyzedText = extractionsActivity.getIntExtra("BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT", 0);
        assertEquals(R.string.analyzed_text, analyzedText);
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