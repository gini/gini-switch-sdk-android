package net.gini.tariffsdk;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.gini.tariffsdk.utils.AutoRotateImageView;

final public class ReviewPictureActivity extends TariffSdkBaseActivity implements
        ReviewPictureContract.View {

    static final String BUNDLE_EXTRA_BUTTON_DISCARD = "BUNDLE_EXTRA_BUTTON_DISCARD";
    static final String BUNDLE_EXTRA_BUTTON_KEEP = "BUNDLE_EXTRA_BUTTON_KEEP";
    static final String BUNDLE_EXTRA_IMAGE_URI = "BUNDLE_EXTRA_IMAGE_URI";
    static final String BUNDLE_EXTRA_TITLE = "BUNDLE_EXTRA_TITLE";
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
    }

    @Override
    public void rotateView() {
        mImagePreview.setRotation(mImagePreview.getRotation() + 90);
    }

    @Override
    public void setImage(final Uri uri) {
        mImagePreview.setImageURI(uri);
    }

    private void checkForUriInBundle() {
        if (getIntent().getExtras() == null || !getIntent().getExtras().containsKey(
                BUNDLE_EXTRA_IMAGE_URI)) {
            throw new IllegalArgumentException("Intent must contain an image Uri");
        }
    }

    private int getDiscardButtonTextFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_DISCARD, R.string.review_discard_button);
    }

    private int getKeepButtonTextFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_KEEP, R.string.review_keep_button);
    }

    private int getTitleFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_TITLE, R.string.review_screen_title);
    }

}