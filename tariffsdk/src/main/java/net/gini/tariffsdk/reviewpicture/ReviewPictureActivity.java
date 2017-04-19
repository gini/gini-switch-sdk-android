package net.gini.tariffsdk.reviewpicture;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import net.gini.tariffsdk.R;
import net.gini.tariffsdk.TariffSdkBaseActivity;
import net.gini.tariffsdk.documentservice.DocumentServiceImpl;

public class ReviewPictureActivity extends TariffSdkBaseActivity implements
        ReviewPictureContract.View {

    private static final String BUNDLE_EXTRA_IMAGE_URI = "BUNDLE_EXTRA_IMAGE_URI";
    private float mDegrees;
    private View mImageContainer;
    private ImageView mImagePreview;
    private ReviewPictureContract.Presenter mPresenter;

    @Override
    public void finishReview() {
        finish();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_review_picture);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        if (getIntent().getExtras() == null || !getIntent().getExtras().containsKey(
                BUNDLE_EXTRA_IMAGE_URI)) {
            throw new IllegalArgumentException("Intent must contain an image Uri");
        }

        final Button discardButton = (Button) findViewById(R.id.button_discard);
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mPresenter.discardImage();
            }
        });
        final Button keepButton = (Button) findViewById(R.id.button_keep);
        keepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mPresenter.keepImage();
            }
        });
        mImagePreview = (ImageView) findViewById(R.id.image_preview);
        mImageContainer = findViewById(R.id.image_container);
        observeViewTree(mImageContainer);

        final Uri uri = getIntent().getExtras().getParcelable(BUNDLE_EXTRA_IMAGE_URI);
        mPresenter = new ReviewPicturePresenter(this, DocumentServiceImpl.getInstance(this), uri);
    }

    @Override
    public void setImage(final Uri uri) {
        mImagePreview.setImageURI(uri);
    }

    @Override
    public void setRotation(final float degrees) {
        mDegrees = degrees;
    }

    public static Intent newIntent(final Context context, final Uri imageUri) {

        final Intent intent = new Intent(context, ReviewPictureActivity.class);
        intent.putExtra(BUNDLE_EXTRA_IMAGE_URI, imageUri);
        return intent;
    }

    private void observeViewTree(@NonNull final View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        rotateImage();
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    private void rotateImage() {
        final int newHeight;
        final int newWidth;
        if (mDegrees % 360 == 90 || mDegrees % 360 == 270) {
            newWidth = mImageContainer.getHeight();
            newHeight = mImageContainer.getWidth();
        } else {
            newWidth = mImageContainer.getWidth();
            newHeight = mImageContainer.getHeight();
        }

        mImagePreview.setRotation(mDegrees);
        FrameLayout.LayoutParams layoutParams =
                (FrameLayout.LayoutParams) mImagePreview.getLayoutParams();
        layoutParams.height = newHeight;
        layoutParams.width = newWidth;


        mImagePreview.requestLayout();
    }
}
