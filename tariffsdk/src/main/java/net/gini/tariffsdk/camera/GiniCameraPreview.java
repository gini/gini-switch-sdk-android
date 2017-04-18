package net.gini.tariffsdk.camera;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class GiniCameraPreview extends SurfaceView {

    public GiniCameraPreview(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GiniCameraPreview(final Context context, final AttributeSet attrs, final int defStyleAttr,
            final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(final Context context, final AttributeSet attrs, final int defStyleAttr,
            final int defStyleRes) {

    }

    public GiniCameraPreview(final Context context) {
        this(context, null);
    }

    public GiniCameraPreview(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

}
