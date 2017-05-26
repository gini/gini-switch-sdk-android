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
import static net.gini.tariffsdk.ExtractionsActivity.BUNDLE_EXTRA_TITLE_TEXT;
import static net.gini.tariffsdk.ReviewPictureActivity.BUNDLE_EXTRA_BUTTON_DISCARD;
import static net.gini.tariffsdk.ReviewPictureActivity.BUNDLE_EXTRA_BUTTON_KEEP;
import static net.gini.tariffsdk.ReviewPictureActivity.BUNDLE_EXTRA_TITLE;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    public void custom_shouldHaveCustomTextColor() {
        mTariffSdk.setAnalyzedTextColor(12345);
        Intent extractionsActivity = getExtractionIntent();
        int color = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR, 0);
        assertEquals(12345, color);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveCustomButtonText() {
        mTariffSdk.setExtractionButtonText(12345);
        Intent extractionsActivity = getExtractionIntent();
        int buttonText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_TEXT, 0);
        assertEquals(12345, buttonText);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveCustomEditTextBackgroundColor() {
        mTariffSdk.setExtractionEditTextBackgroundColor(12345);
        Intent extractionsActivity = getExtractionIntent();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_EDIT_TEXT_BACKGROUND_COLOR, 0);
        assertEquals(12345, textColor);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveCustomEditTextColor() {
        mTariffSdk.setExtractionEditTextColor(12345);
        Intent extractionsActivity = getExtractionIntent();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_EDIT_TEXT_COLOR, 0);
        assertEquals(12345, textColor);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveCustomHintTextColor() {
        mTariffSdk.setExtractionHintColor(12345);
        Intent extractionsActivity = getExtractionIntent();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_HINT_COLOR, 0);
        assertEquals(12345, textColor);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveCustomImage() {
        mTariffSdk.setAnalyzedImage(12345);
        Intent extractionsActivity = getExtractionIntent();
        int image = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE, 0);
        assertEquals(12345, image);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveCustomLineColor() {
        mTariffSdk.setExtractionLineColor(12345);
        Intent extractionsActivity = getExtractionIntent();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_LINE_COLOR, 0);
        assertEquals(12345, textColor);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveCustomText() {
        mTariffSdk.setAnalyzedText(12345);
        Intent extractionsActivity = getExtractionIntent();
        int analyzedText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT, 0);
        assertEquals(12345, analyzedText);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveCustomTextSize() {
        mTariffSdk.setAnalyzedTextSize(16);
        Intent extractionsActivity = getExtractionIntent();
        int textSize = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE, 0);
        assertEquals(16, textSize);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveCustomTitleText() {
        mTariffSdk.setExtractionTitleText(12345);
        Intent extractionsActivity = getExtractionIntent();
        int titleText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_TITLE_TEXT, 0);
        assertEquals(12345, titleText);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveDefaultButtonText() {
        Intent extractionsActivity = getExtractionIntent();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_TEXT, 0);
        assertEquals(R.string.button_extractions, textColor);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveDefaultEditTextBackgroundColor() {
        Intent extractionsActivity = getExtractionIntent();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_EDIT_TEXT_BACKGROUND_COLOR, 0);
        assertEquals(R.color.secondaryColor, textColor);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveDefaultEditTextColor() {
        Intent extractionsActivity = getExtractionIntent();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_EDIT_TEXT_COLOR, 0);
        assertEquals(R.color.primaryText, textColor);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveDefaultHintTextColor() {
        Intent extractionsActivity = getExtractionIntent();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_HINT_COLOR, 0);
        assertEquals(R.color.secondaryText, textColor);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveDefaultImage() {
        Intent extractionsActivity = getExtractionIntent();
        int image = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE, 0);
        assertEquals(R.drawable.ic_check_circle, image);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveDefaultLineColor() {
        Intent extractionsActivity = getExtractionIntent();
        int textColor = extractionsActivity.getIntExtra(BUNDLE_EXTRA_LINE_COLOR, 0);
        assertEquals(R.color.primaryText, textColor);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveDefaultText() {
        Intent extractionsActivity = getExtractionIntent();
        int analyzedText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT, 0);
        assertEquals(R.string.analyzed_text, analyzedText);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveDefaultTextColor() {
        Intent extractionsActivity = getExtractionIntent();
        int color = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR, 0);
        assertEquals(R.color.primaryText, color);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveDefaultTextSize() {
        Intent extractionsActivity = getExtractionIntent();
        int textSize = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE, 0);
        int defaultSize = mContext.getResources().getInteger(R.integer.analyzed_text_size);
        assertEquals(defaultSize, textSize);
    }

    @Test
    @SmallTest
    public void extractionsIntent_shouldHaveDefaultTitleText() {
        Intent extractionsActivity = getExtractionIntent();
        int titleText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_TITLE_TEXT, 0);
        assertEquals(R.string.extractions_title, titleText);
    }

    @Test
    @SmallTest
    public void reviewIntent_shouldHaveCustomDiscardText() {
        mTariffSdk.setReviewDiscardText(12345);
        Intent extractionsActivity = getReviewIntent();
        int analyzedText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_DISCARD, 0);
        assertEquals(12345, analyzedText);
    }

    @Test
    @SmallTest
    public void reviewIntent_shouldHaveCustomKeepText() {
        mTariffSdk.setReviewKeepText(12345);
        Intent extractionsActivity = getReviewIntent();
        int analyzedText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_KEEP, 0);
        assertEquals(12345, analyzedText);
    }

    @Test
    @SmallTest
    public void reviewIntent_shouldHaveCustomTitleText() {
        mTariffSdk.setReviewTitleText(12345);
        Intent extractionsActivity = getReviewIntent();
        int analyzedText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_TITLE, 0);
        assertEquals(12345, analyzedText);
    }

    @Test
    @SmallTest
    public void reviewIntent_shouldHaveDefaultDiscardText() {
        Intent extractionsActivity = getReviewIntent();
        int titleText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_DISCARD, 0);
        assertEquals(R.string.review_discard_button, titleText);
    }

    @Test
    @SmallTest
    public void reviewIntent_shouldHaveDefaultKeepText() {
        Intent extractionsActivity = getReviewIntent();
        int titleText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_KEEP, 0);
        assertEquals(R.string.review_keep_button, titleText);
    }

    @Test
    @SmallTest
    public void reviewIntent_shouldHaveDefaultTitleText() {
        Intent extractionsActivity = getReviewIntent();
        int titleText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_TITLE, 0);
        assertEquals(R.string.review_screen_title, titleText);
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

    private Intent getExtractionIntent() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        return intentFactory.createExtractionsActivity();
    }

    private Intent getReviewIntent() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        return intentFactory.createReviewActivity(Uri.EMPTY);
    }
}