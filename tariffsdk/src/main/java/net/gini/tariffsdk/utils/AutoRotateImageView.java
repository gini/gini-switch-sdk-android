package net.gini.tariffsdk.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.media.ExifInterface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.IOException;

public class AutoRotateImageView extends FrameLayout implements BitmapMemoryCache.BitmapListener {

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
        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addView(mImageView);
        observeViewTree(this);
    }

    @Override
    public void bitmapLoaded(@Nullable final Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void displayImage(@Nullable final Uri uri) {
        mDegrees = getRequiredRotationDegrees(uri);
        mUri = uri;
        setImageBitmap();
        rotateImage();
    }

    public void setImageURI(@Nullable final Uri uri) {
        mDegrees = getRequiredRotationDegrees(uri);
        mUri = uri;
        observeViewTree(this);
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
        if (mUri != null) {
            if (shouldUseThumbnail()) {
                BitmapMemoryCache.getInstance().loadThumbnailAsync(mUri, getHeight(), getWidth(),
                        getContext(), this);
            } else {
                BitmapMemoryCache.getInstance().loadBitmapAsync(mUri, getHeight(), getWidth(),
                        getContext(), this);
            }
        } else {
            bitmapLoaded(null);
        }
    }

    //if image height is smaller than 100 dp
    private boolean shouldUseThumbnail() {
        return getHeight() < TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                getContext().getResources().getDisplayMetrics());
    }
}
