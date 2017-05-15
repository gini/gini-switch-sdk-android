package net.gini.tariffsdk;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;

import net.gini.tariffsdk.utils.AutoRotateImageView;

final public class ReviewPictureActivity extends TariffSdkBaseActivity implements
        ReviewPictureContract.View {

    static final String BUNDLE_EXTRA_IMAGE_URI = "BUNDLE_EXTRA_IMAGE_URI";
    private AutoRotateImageView mImagePreview;
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
        mImagePreview = (AutoRotateImageView) findViewById(R.id.image_preview);
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
        }

        if (hasCustomButtonTextColor()) {
            int customButtonTextColor = getButtonTextColorResourceIdFromBundle();
            int textColor = ContextCompat.getColor(this, customButtonTextColor);
            discardButton.setTextColor(textColor);
            keepButton.setTextColor(textColor);
        }
    }

    @Override
    public void rotateView() {
        mImagePreview.setRotation(mImagePreview.getRotation() + 90);
    }

    @Override
    public void setImage(final Uri uri) {
        mImagePreview.setImageURI(uri);
    }

}
