package net.gini.tariffsdk.reviewpicture;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.gini.tariffsdk.R;

public class ReviewPictureView extends RelativeLayout implements ReviewPictureContract.View {

    private final ImageView mImagePreview;
    private ReviewPictureContract.Presenter mPresenter;

    public ReviewPictureView(final Context context) {
        this(context, null);
    }

    public ReviewPictureView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        final View view = inflate(context, R.layout.view_review_picture, this);

        final Button discardButton = (Button) view.findViewById(R.id.button_discard);
        discardButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                mPresenter.discardImage();
            }
        });
        final Button keepButton = (Button) view.findViewById(R.id.button_keep);
        keepButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                mPresenter.keepImage();
            }
        });
        mImagePreview = (ImageView) view.findViewById(R.id.image_preview);
    }

    @Override
    public void finishReview() {
        ((Activity)getContext()).finish();
    }

    @Override
    public void setImage(final Uri uri) {
        mImagePreview.setImageURI(uri);
    }

    @Override
    public void setPresenter(final ReviewPictureContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
