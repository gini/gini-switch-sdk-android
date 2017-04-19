package net.gini.tariffsdk.reviewpicture;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import net.gini.tariffsdk.R;
import net.gini.tariffsdk.TariffSdkBaseActivity;
import net.gini.tariffsdk.documentservice.DocumentServiceImpl;

public class ReviewPictureActivity extends TariffSdkBaseActivity {

    private static final String BUNDLE_EXTRA_IMAGE_URI = "BUNDLE_EXTRA_IMAGE_URI";
    private ReviewPicturePresenter mPresenter;

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


        final Uri uri = getIntent().getExtras().getParcelable(BUNDLE_EXTRA_IMAGE_URI);
        ReviewPictureView view = (ReviewPictureView) findViewById(R.id.review_picture_view);
        mPresenter = new ReviewPicturePresenter(view, DocumentServiceImpl.getInstance(this), uri);
    }

    public static Intent newIntent(final Context context, final Uri imageUri) {

        final Intent intent = new Intent(context, ReviewPictureActivity.class);
        intent.putExtra(BUNDLE_EXTRA_IMAGE_URI, imageUri);
        return intent;
    }
}
