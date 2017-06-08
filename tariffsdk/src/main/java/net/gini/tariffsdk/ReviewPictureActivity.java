package net.gini.tariffsdk;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

final public class ReviewPictureActivity extends TariffSdkBaseActivity implements
        ReviewPictureContract.View {

    static final String BUNDLE_EXTRA_BUTTON_DISCARD = "BUNDLE_EXTRA_BUTTON_DISCARD";
    static final String BUNDLE_EXTRA_BUTTON_KEEP = "BUNDLE_EXTRA_BUTTON_KEEP";
    static final String BUNDLE_EXTRA_IMAGE_URI = "BUNDLE_EXTRA_IMAGE_URI";
    static final String BUNDLE_EXTRA_TITLE = "BUNDLE_EXTRA_TITLE";
    private ImageView mImagePreview;
    private FrameLayout mImagePreviewContainer;
    private ReviewPictureContract.Presenter mPresenter;
    private float mViewRotationInDegrees;

    @Override
    public void finishReview() {
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
        final View rotateButton = findViewById(R.id.button_rotate);
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mPresenter.rotateImage();
            }
        });

        final Uri uri = getIntent().getExtras().getParcelable(BUNDLE_EXTRA_IMAGE_URI);
        mPresenter = new ReviewPicturePresenter(this, TariffSdk.getSdk().getDocumentService(),
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

        mImagePreviewContainer.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        rotateView(mViewRotationInDegrees);
                        mImagePreviewContainer.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                    }
                });

    }

    @Override
    public void rotateView() {
        mViewRotationInDegrees += 90;
        rotateViewAnimated(mViewRotationInDegrees);
    }

    @Override
    public void setImage(final Uri uri) {
        mViewRotationInDegrees = getRequiredRotationDegrees(uri);
        mImagePreview.setImageURI(uri);
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
            final String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            switch (orientation) {
                case "3":
                    return 180;
                case "0":
                case "6":
                    return 90;
                case "8":
                    return 270;
            }

        } catch (IOException ignored) {
        }

        return 0;
    }

    private int getTitleFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_TITLE, R.string.review_screen_title);
    }

    private void rotateView(final float viewRotationInDegrees) {
        final ValueAnimator widthAnimation = createWidthValueAnimator(viewRotationInDegrees);
        final ValueAnimator heightAnimation = createHeightValueAnimator(viewRotationInDegrees);

        addWidthUpdateListener(widthAnimation);
        addHeightUpdateListener(heightAnimation);

        final ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(mImagePreview, "rotation",
                viewRotationInDegrees);
        rotateAnimation.setDuration(0);
        heightAnimation.setDuration(0);
        widthAnimation.setDuration(0);

        widthAnimation.start();
        heightAnimation.start();
        rotateAnimation.start();
    }

    private void rotateViewAnimated(final float degrees) {
        final ValueAnimator widthAnimation = createWidthValueAnimator(degrees);
        final ValueAnimator heightAnimation = createHeightValueAnimator(degrees);

        addWidthUpdateListener(widthAnimation);
        addHeightUpdateListener(heightAnimation);

        final ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(mImagePreview, "rotation",
                degrees);
        widthAnimation.start();
        heightAnimation.start();
        rotateAnimation.start();
    }

}
