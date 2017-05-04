package net.gini.tariffsdk.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.IOException;

public class AutoRotateImageView extends FrameLayout {

    private final ImageView mImageView;
    private float mDegrees;
    @Nullable
    private Uri mUri;

    public AutoRotateImageView(final Context context) {
        this(context, null);
    }

    public AutoRotateImageView(final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);

        mImageView = new ImageView(context, attrs);
        LayoutParams paramsImageView = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        paramsImageView.gravity = Gravity.CENTER;
        mImageView.setLayoutParams(paramsImageView);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(mImageView);
        observeViewTree(this);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setImageURI(@Nullable final Uri uri) {
        mDegrees = getRequiredRotationDegrees(uri);
        mUri = uri;
    }

    private float getRequiredRotationDegrees(final Uri imageUri) {

        final ExifInterface exif;
        try {
            exif = new ExifInterface(imageUri.getPath());
            final String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            switch (orientation) {
                case "3":
                    return 180;
                case "0":
                case "6":
                    return 90;
                case "8":
                    return 270;
            }

        } catch (IOException ignored) {
        }

        return 0;
    }

    private void observeViewTree(@NonNull final View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        setImageBitmap();
                        rotateImage();
                    }
                });
    }

    private void rotateImage() {
        final int newHeight;
        final int newWidth;
        if (mDegrees % 360 == 90 || mDegrees % 360 == 270) {
            newWidth = getHeight();
            newHeight = getWidth();
        } else {
            newWidth = getWidth();
            newHeight = getHeight();
        }

        mImageView.setRotation(mDegrees);
        FrameLayout.LayoutParams layoutParams =
                (FrameLayout.LayoutParams) mImageView.getLayoutParams();
        layoutParams.height = newHeight;
        layoutParams.width = newWidth;


        mImageView.requestLayout();
    }

    private void setImageBitmap() {
        BitmapMemoryCache.BitmapListener listener = new BitmapMemoryCache.BitmapListener() {
            @Override
            public void bitmapLoaded(@NonNull final Bitmap bitmap) {
                mImageView.setImageBitmap(bitmap);
            }
        };
        BitmapMemoryCache.getInstance().loadBitmapAsync(mUri, mImageView.getHeight(),
                mImageView.getWidth(), getContext(), listener);
    }
}
