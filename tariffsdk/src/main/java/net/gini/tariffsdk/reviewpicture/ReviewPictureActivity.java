package net.gini.tariffsdk.reviewpicture;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;

import net.gini.tariffsdk.utils.AutoRotateImageView;
import net.gini.tariffsdk.R;
import net.gini.tariffsdk.TariffSdkBaseActivity;
import net.gini.tariffsdk.documentservice.DocumentServiceImpl;

public class ReviewPictureActivity extends TariffSdkBaseActivity implements
        ReviewPictureContract.View {

    private static final String BUNDLE_EXTRA_IMAGE_URI = "BUNDLE_EXTRA_IMAGE_URI";
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

        final Uri uri = getIntent().getExtras().getParcelable(BUNDLE_EXTRA_IMAGE_URI);
        mPresenter = new ReviewPicturePresenter(this, DocumentServiceImpl.getInstance(this), uri);
    }

    @Override
    public void setImage(final Uri uri) {
        mImagePreview.setImageURI(uri);
    }

    public static Intent newIntent(final Context context, final Uri imageUri) {

        final Intent intent = new Intent(context, ReviewPictureActivity.class);
        intent.putExtra(BUNDLE_EXTRA_IMAGE_URI, imageUri);
        return intent;
    }
}
