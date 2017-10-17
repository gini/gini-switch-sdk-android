package net.gini.switchsdk;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.media.ExifInterface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.gini.switchsdk.onboarding.OnboardingAdapter;
import net.gini.switchsdk.utils.ExifUtils;

import java.io.IOException;

final public class ReviewPictureActivity extends SwitchSdkBaseActivity implements
        ReviewPictureContract.View {

    static final String BUNDLE_EXTRA_BUTTON_DISCARD = "BUNDLE_EXTRA_BUTTON_DISCARD";
    static final String BUNDLE_EXTRA_BUTTON_KEEP = "BUNDLE_EXTRA_BUTTON_KEEP";
    static final String BUNDLE_EXTRA_IMAGE_URI = "BUNDLE_EXTRA_IMAGE_URI";
    static final String BUNDLE_EXTRA_TITLE = "BUNDLE_EXTRA_TITLE";
    static final int RESULT_CODE_KEEP = 1;
    static final int RESULT_CODE_DISCARD = 2;
    private static final String STATE_KEY_ROTATION = "STATE_KEY_ROTATION";
    private static final String STATE_KEY_VIEW_ROTATION = "STATE_KEY_VIEW_ROTATION";
    private View mHelpContainer;
    private ImageView mImagePreview;
    private FrameLayout mImagePreviewContainer;
    private ReviewPictureContract.Presenter mPresenter;
    private View mRotateButton;
    private float mViewRotationInDegrees;

    @Override
    public void discardImageAndFinishReview() {
        setResult(RESULT_CODE_DISCARD);
        finish();
    }

    @Override
    public void keepImageAndFinishReview() {
        setResult(RESULT_CODE_KEEP);
        finish();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_review_picture);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        colorToolbar(toolbar);

        TextView title = (TextView) toolbar.getChildAt(0);
        title.setText(getTitleFromBundle());

        checkForUriInBundle();

        final Button discardButton = (Button) findViewById(R.id.button_discard);
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mPresenter.discardImage();
            }
        });
        discardButton.setText(getDiscardButtonTextFromBundle());
        final Button keepButton = (Button) findViewById(R.id.button_keep);
        keepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mPresenter.keepImage();
            }
        });
        keepButton.setText(getKeepButtonTextFromBundle());
        mImagePreview = (ImageView) findViewById(R.id.image_preview);
        mImagePreviewContainer = (FrameLayout) findViewById(R.id.image_preview_container);
        mRotateButton = findViewById(R.id.button_rotate);
        mRotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mPresenter.rotateImage();
            }
        });

        final Uri uri = getIntent().getExtras().getParcelable(BUNDLE_EXTRA_IMAGE_URI);
        mPresenter = new ReviewPicturePresenter(this, SwitchSdk.getSdk().getDocumentService(),
                uri);


        if (hasCustomButtonStyleSet()) {
            int customButtonStyle = getButtonStyleResourceIdFromBundle();
            discardButton.setBackgroundResource(customButtonStyle);
            keepButton.setBackgroundResource(customButtonStyle);
        } else {
            ViewCompat.setBackgroundTintList(keepButton,
                    ContextCompat.getColorStateList(this, R.color.positiveColor));
            ViewCompat.setBackgroundTintList(discardButton,
                    ContextCompat.getColorStateList(this, R.color.negativeColor));

        }

        if (hasCustomButtonTextColor()) {
            int customButtonTextColor = getButtonTextColorResourceIdFromBundle();
            int textColor = ContextCompat.getColor(this, customButtonTextColor);
            discardButton.setTextColor(textColor);
            keepButton.setTextColor(textColor);
        }

        if (savedInstanceState != null) {
            mViewRotationInDegrees = savedInstanceState.getFloat(STATE_KEY_VIEW_ROTATION);
        }

        mImagePreviewContainer.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        rotateView(mViewRotationInDegrees, 0);
                        mImagePreviewContainer.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                    }
                });

        setUpHelpScreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getIntent().putExtra(STATE_KEY_ROTATION, mPresenter.getRotation());
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            final int rotation = savedInstanceState.getInt(STATE_KEY_ROTATION, 0);
            getIntent().putExtra(STATE_KEY_ROTATION, rotation);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final int rotation = getIntent().getIntExtra(STATE_KEY_ROTATION, 0);
        mPresenter.setRotation(rotation);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_KEY_ROTATION, mPresenter.getRotation());
        outState.putFloat(STATE_KEY_VIEW_ROTATION, mViewRotationInDegrees);
    }

    @Override
    public void rotateViewAnimated() {
        mViewRotationInDegrees += 90;
        rotateView(mViewRotationInDegrees, 300);
    }

    @Override
    public void setImage(final Uri uri) {
        mViewRotationInDegrees = getRequiredRotationDegrees(uri);
        mImagePreview.setImageURI(uri);
    }

    @Override
    protected void showHelpDialog() {
        mHelpContainer.setVisibility(View.VISIBLE);
        mRotateButton.setVisibility(View.GONE);
    }

    private void addHeightUpdateListener(final ValueAnimator heightAnimation) {
        heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int height = (int) valueAnimator.getAnimatedValue();
                FrameLayout.LayoutParams layoutParams =
                        (FrameLayout.LayoutParams) mImagePreview.getLayoutParams();
                layoutParams.height = height;
                mImagePreview.requestLayout();
            }
        });
    }

    private void addWidthUpdateListener(final ValueAnimator widthAnimation) {
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int width = (int) valueAnimator.getAnimatedValue();
                FrameLayout.LayoutParams layoutParams =
                        (FrameLayout.LayoutParams) mImagePreview.getLayoutParams();
                layoutParams.width = width;
                mImagePreview.requestLayout();
            }
        });
    }

    private void checkForUriInBundle() {
        if (getIntent().getExtras() == null || !getIntent().getExtras().containsKey(
                BUNDLE_EXTRA_IMAGE_URI)) {
            throw new IllegalArgumentException("Intent must contain an image Uri");
        }
    }

    private ValueAnimator createHeightValueAnimator(final float degrees) {
        return degrees % 360 == 90 || degrees % 360 == 270 ? ValueAnimator.ofInt(
                mImagePreview.getHeight(), mImagePreviewContainer.getWidth()) : ValueAnimator.ofInt(
                mImagePreview.getHeight(), mImagePreviewContainer.getHeight());
    }

    private ValueAnimator createWidthValueAnimator(final float degrees) {
        return degrees % 360 == 90 || degrees % 360 == 270 ? ValueAnimator.ofInt(
                mImagePreview.getWidth(), mImagePreviewContainer.getHeight()) : ValueAnimator.ofInt(
                mImagePreview.getWidth(), mImagePreviewContainer.getWidth());
    }

    private int getDiscardButtonTextFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_DISCARD, R.string.review_discard_button);
    }

    private int getKeepButtonTextFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_KEEP, R.string.review_keep_button);
    }

    private float getRequiredRotationDegrees(final Uri imageUri) {
        final ExifInterface exif;
        try {
            exif = new ExifInterface(imageUri.getPath());
            final int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            return ExifUtils.getDegreesFromExif(orientation);
        } catch (IOException ignored) {
        }
        return 0;
    }

    private int getTitleFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_TITLE, R.string.review_screen_title);
    }

    private void rotateView(final float viewRotationInDegrees, final long duration) {
        final ValueAnimator widthAnimation = createWidthValueAnimator(viewRotationInDegrees);
        final ValueAnimator heightAnimation = createHeightValueAnimator(viewRotationInDegrees);

        addWidthUpdateListener(widthAnimation);
        addHeightUpdateListener(heightAnimation);

        final ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(mImagePreview, "rotation",
                viewRotationInDegrees);
        rotateAnimation.setDuration(duration);
        heightAnimation.setDuration(duration);
        widthAnimation.setDuration(duration);

        widthAnimation.start();
        heightAnimation.start();
        rotateAnimation.start();
    }

    private void setUpHelpScreen() {
        mHelpContainer = findViewById(R.id.helpContainer);
        mHelpContainer.setVisibility(View.GONE);
        final ViewPager onboardingViewPager = (ViewPager) findViewById(R.id.onBoardingViewPager);
        final OnboardingAdapter adapter = new OnboardingAdapter(this);
        onboardingViewPager.setAdapter(adapter);
        final TabLayout onboardingTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        onboardingTabLayout.setupWithViewPager(onboardingViewPager, true);
        onboardingViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int state) {
            }

            @Override
            public void onPageScrolled(final int position, final float positionOffset,
                    final int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                if (adapter.isLastItem(position)) {
                    onboardingViewPager.setCurrentItem(0, false);
                    mHelpContainer.setVisibility(View.GONE);
                    mRotateButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
