package net.gini.tariffsdk.camera;


import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class CameraSurfacePreview extends SurfaceView {

    @SuppressWarnings("deprecation") //since camera1 api is deprecated, but we support versions < 21
    private Camera.Size mPreviewSize;
    private ScaleType mScaleType = ScaleType.CENTER_INSIDE;

    public CameraSurfacePreview(Context context) {
        super(context);
    }

    public CameraSurfacePreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraSurfacePreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getWidth();
        int height = getHeight();

        if (width > 0 && height > 0 &&
                mPreviewSize != null) {
            float aspectRatioSurface = (float) width / (float) height;
            // Preview size is in landscape, we need it in portrait, switching height and width
            float aspectRatioPreview = (float) mPreviewSize.height / (float) mPreviewSize.width;

            int adjustedWidth = width;
            int adjustedHeight = height;

            switch (mScaleType) {
                case CENTER_RESIZE:
                    if (aspectRatioSurface < aspectRatioPreview) {
                        // surface width < preview width AND surface height > preview height
                        // Keep the height and change the width to resize the surface to the
                        // preview's aspect ratio
                        adjustedWidth = (int) (height * aspectRatioPreview);
                    } else if (aspectRatioSurface > aspectRatioPreview) {
                        // surface width > preview width AND surface height < preview height
                        // Keep the width and change the height to resize the surface to the
                        // preview's aspect ratio
                        adjustedHeight = (int) (width / aspectRatioPreview);
                    }
                    break;
                case CENTER_INSIDE:
                    if (aspectRatioSurface < aspectRatioPreview) {
                        // surface width < preview width AND surface height > preview height
                        // Keep the width and change the height to fit the preview inside the
                        // surface's original size
                        adjustedHeight = (int) (width / aspectRatioPreview);
                    } else if (aspectRatioSurface > aspectRatioPreview) {
                        // surface width > preview width AND surface height < preview height
                        // Keep the height and change the width to fit the preview inside the
                        // surface's original size
                        adjustedWidth = (int) (height * aspectRatioPreview);
                    }
                    break;
            }

            setMeasuredDimension(adjustedWidth, adjustedHeight);
        }
    }

    @SuppressWarnings("deprecation")//since camera1 api is deprecated, but we support versions < 21
    public void setPreviewSize(Camera.Size previewSize) {
        this.mPreviewSize = previewSize;
        requestLayout();
    }

    public void setScaleType(ScaleType scaleType) {
        mScaleType = scaleType;
        requestLayout();
    }

    public enum ScaleType {
        CENTER_RESIZE, CENTER_INSIDE
    }
}