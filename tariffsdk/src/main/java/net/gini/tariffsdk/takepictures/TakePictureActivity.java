package net.gini.tariffsdk.takepictures;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import net.gini.tariffsdk.R;
import net.gini.tariffsdk.TariffSdkBaseActivity;
import net.gini.tariffsdk.documentservice.DocumentService;

final public class TakePictureActivity extends TariffSdkBaseActivity {

    private TakePicturePresenter mPresenter;

    public static Intent newIntent(final Context context,
            final int themeResourceId) {


        Intent intent = new Intent(context, TakePictureActivity.class);
        intent.putExtra(BUNDLE_EXTRA_THEME, themeResourceId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_take_picture);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        final TakePictureView view = (TakePictureView) findViewById(R.id.take_picture_view);
        DocumentService documentService = null;
        mPresenter = new TakePicturePresenter(view, documentService);
        view.setPresenter(mPresenter);
    }

}
