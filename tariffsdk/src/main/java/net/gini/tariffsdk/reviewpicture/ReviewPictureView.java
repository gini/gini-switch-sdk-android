package net.gini.tariffsdk.reviewpicture;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.gini.tariffsdk.R;

public class ReviewPictureView extends RelativeLayout implements ReviewPictureContract.View {

    private final View mImageContainer;
    private final ImageView mImagePreview;
    private float mDegrees;
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
        mImageContainer = view.findViewById(R.id.image_container);
        observeViewTree(this);
    }

    @Override
    public void finishReview() {
        ((Activity) getContext()).finish();
    }

    @Override
    public void setImage(final Uri uri) {
        mImagePreview.invalidate();
        mImagePreview.setImageURI(uri);
    }

    @Override
    public void setPresenter(final ReviewPictureContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setRotation(final float degrees) {
        mDegrees = degrees;
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
