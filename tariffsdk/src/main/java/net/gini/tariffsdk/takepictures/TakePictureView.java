package net.gini.tariffsdk.takepictures;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import net.gini.tariffsdk.R;

import java.util.List;

public class TakePictureView extends LinearLayout implements TakePictureContract.View {

    private final ImageButton mTakePictureButton;
    private TakePictureContract.Presenter mPresenter;

    public TakePictureView(final Context context) {
        this(context, null);
    }

    public TakePictureView(final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);

        final View view = inflate(context, R.layout.view_take_picture, this);

        setOrientation(VERTICAL);

        mTakePictureButton = (ImageButton) view.findViewById(R.id.button_take_picture);

    }

    @Override
    public void setPresenter(final TakePictureContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
