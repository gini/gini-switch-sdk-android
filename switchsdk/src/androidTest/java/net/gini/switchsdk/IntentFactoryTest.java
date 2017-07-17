package net.gini.switchsdk;

import static junit.framework.Assert.assertEquals;

import static net.gini.switchsdk.ReviewPictureActivity.BUNDLE_EXTRA_BUTTON_DISCARD;
import static net.gini.switchsdk.ReviewPictureActivity.BUNDLE_EXTRA_BUTTON_KEEP;
import static net.gini.switchsdk.ReviewPictureActivity.BUNDLE_EXTRA_TITLE;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_PREVIEW_FAILED_TEXT;
import static net.gini.switchsdk.TakePictureActivity.BUNDLE_EXTRA_PREVIEW_SUCCESS_TEXT;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import net.gini.switchsdk.network.TariffApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class IntentFactoryTest {

    private static final int DUMMY_RESOURCE_ID = 12345;
    private Context mContext;
    private TariffSdk mTariffSdk;

    @Test
    @SmallTest
    public void previewIntent_shouldHaveCustomFailedText() {
        mTariffSdk.setPreviewFailedText(DUMMY_RESOURCE_ID);
        Intent previewActivity = getPreviewIntent();
        int titleText = previewActivity.getIntExtra(BUNDLE_EXTRA_PREVIEW_FAILED_TEXT, 0);
        assertEquals(DUMMY_RESOURCE_ID, titleText);
    }

    @Test
    @SmallTest
    public void previewIntent_shouldHaveCustomImage() {
        mTariffSdk.setAnalyzedImage(DUMMY_RESOURCE_ID);
        Intent extractionsActivity = getPreviewIntent();
        int image = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE, 0);
        assertEquals(DUMMY_RESOURCE_ID, image);
    }

    @Test
    @SmallTest
    public void previewIntent_shouldHaveCustomSuccessText() {
        mTariffSdk.setPreviewSuccessText(DUMMY_RESOURCE_ID);
        Intent previewActivity = getPreviewIntent();
        int titleText = previewActivity.getIntExtra(BUNDLE_EXTRA_PREVIEW_SUCCESS_TEXT, 0);
        assertEquals(DUMMY_RESOURCE_ID, titleText);
    }

    @Test
    @SmallTest
    public void previewIntent_shouldHaveCustomText() {
        mTariffSdk.setAnalyzedText(DUMMY_RESOURCE_ID);
        Intent extractionsActivity = getPreviewIntent();
        int analyzedText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT, 0);
        assertEquals(DUMMY_RESOURCE_ID, analyzedText);
    }

    @Test
    @SmallTest
    public void previewIntent_shouldHaveCustomTextColor() {
        mTariffSdk.setAnalyzedTextColor(DUMMY_RESOURCE_ID);
        Intent extractionsActivity = getPreviewIntent();
        int color = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR, 0);
        assertEquals(DUMMY_RESOURCE_ID, color);
    }

    @Test
    @SmallTest
    public void previewIntent_shouldHaveCustomTextSize() {
        mTariffSdk.setAnalyzedTextSize(16);
        Intent extractionsActivity = getPreviewIntent();
        int textSize = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE, 0);
        assertEquals(16, textSize);
    }

    @Test
    @SmallTest
    public void previewIntent_shouldHaveDefaultFailedText() {
        Intent previewActivity = getPreviewIntent();
        int titleText = previewActivity.getIntExtra(BUNDLE_EXTRA_PREVIEW_FAILED_TEXT, 0);
        assertEquals(R.string.preview_analyze_failed, titleText);
    }

    @Test
    @SmallTest
    public void previewIntent_shouldHaveDefaultImage() {
        Intent extractionsActivity = getPreviewIntent();
        int image = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE, 0);
        assertEquals(R.drawable.ic_check_circle, image);
    }

    @Test
    @SmallTest
    public void previewIntent_shouldHaveDefaultSuccessText() {
        Intent previewActivity = getPreviewIntent();
        int titleText = previewActivity.getIntExtra(BUNDLE_EXTRA_PREVIEW_SUCCESS_TEXT, 0);
        assertEquals(R.string.preview_analyze_success, titleText);
    }

    @Test
    @SmallTest
    public void previewIntent_shouldHaveDefaultText() {
        Intent extractionsActivity = getPreviewIntent();
        int analyzedText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT, 0);
        assertEquals(R.string.analyzed_text, analyzedText);
    }

    @Test
    @SmallTest
    public void previewIntent_shouldHaveDefaultTextColor() {
        Intent extractionsActivity = getPreviewIntent();
        int color = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR, 0);
        assertEquals(R.color.primaryText, color);
    }

    @Test
    @SmallTest
    public void previewIntent_shouldHaveDefaultTextSize() {
        Intent extractionsActivity = getPreviewIntent();
        int textSize = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE, 0);
        int defaultSize = mContext.getResources().getInteger(R.integer.analyzed_text_size);
        assertEquals(defaultSize, textSize);
    }

    @Test
    @SmallTest
    public void reviewIntent_shouldHaveCustomDiscardText() {
        mTariffSdk.setReviewDiscardText(DUMMY_RESOURCE_ID);
        Intent extractionsActivity = getReviewIntent();
        int analyzedText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_DISCARD, 0);
        assertEquals(DUMMY_RESOURCE_ID, analyzedText);
    }

    @Test
    @SmallTest
    public void reviewIntent_shouldHaveCustomKeepText() {
        mTariffSdk.setReviewKeepText(DUMMY_RESOURCE_ID);
        Intent extractionsActivity = getReviewIntent();
        int analyzedText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_BUTTON_KEEP, 0);
        assertEquals(DUMMY_RESOURCE_ID, analyzedText);
    }

    @Test
    @SmallTest
    public void reviewIntent_shouldHaveCustomTitleText() {
        mTariffSdk.setReviewTitleText(DUMMY_RESOURCE_ID);
        Intent extractionsActivity = getReviewIntent();
        int analyzedText = extractionsActivity.getIntExtra(BUNDLE_EXTRA_TITLE, 0);
        assertEquals(DUMMY_RESOURCE_ID, analyzedText);
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
        final TariffApi mockApi = Mockito.mock(TariffApi.class);
        mTariffSdk = TariffSdk.create(mContext, null, null, new RemoteConfigManager(mockApi));
    }

    @After
    public void tearDown() {
        TariffSdk.mSingleton = null;
    }

    private Intent getPreviewIntent() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        return intentFactory.createTariffSdkIntent();
    }

    private Intent getReviewIntent() {
        final IntentFactory intentFactory = new IntentFactory(mTariffSdk);
        return intentFactory.createReviewActivity(Uri.EMPTY);
    }
}