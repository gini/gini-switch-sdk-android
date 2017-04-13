package net.gini.tariffsdk.reviewpicture;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import net.gini.tariffsdk.R;
import net.gini.tariffsdk.TariffSdkBaseActivity;

public class ReviewPictureActivity extends TariffSdkBaseActivity {

    private ReviewPicturePresenter mPresenter;

    public static Intent newIntent(Context context) {

        final Intent intent = new Intent(context, ReviewPictureActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_review_picture);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        ReviewPictureView view = (ReviewPictureView) findViewById(R.id.review_picture_view);
        mPresenter = new ReviewPicturePresenter(view);
    }

}
