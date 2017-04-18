package net.gini.tariffsdk.takepictures;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.gini.tariffsdk.R;
import net.gini.tariffsdk.camera.Camera1;
import net.gini.tariffsdk.camera.GiniCamera;
import net.gini.tariffsdk.camera.GiniCameraPreview;
import net.gini.tariffsdk.reviewpicture.ReviewPictureActivity;

class TakePictureView extends LinearLayout implements TakePictureContract.View {

    private GiniCamera mCamera;
    private GiniCameraPreview mCameraPreview;
    private TakePictureContract.Presenter mPresenter;
    private ImageButton mTakePictureButton;

    public TakePictureView(final Context context) {
        this(context, null);
    }

    public TakePictureView(final Context context,
            @Nullable final AttributeSet attrs) {
        super(context, attrs);

        final View view = inflate(context, R.layout.view_take_picture, this);

        setOrientation(VERTICAL);

        mTakePictureButton = (ImageButton) view.findViewById(R.id.button_take_picture);
        mTakePictureButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
                Intent intent = ReviewPictureActivity.newIntent(context);
                context.startActivity(intent);
            }
        });

        mCameraPreview = (GiniCameraPreview) view.findViewById(R.id.camera_preview);


    }

    @Override
    public void initCamera() {
        final WindowManager windowManager =
                (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mCamera = new Camera1(mCameraPreview);
        mCamera.setPreviewOrientation(GiniCamera.Orientation.PORTRAIT,
                windowManager.getDefaultDisplay().getRotation());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(mCamera != null) {
            mCamera.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mCamera != null) {
            mCamera.stop();
        }
    }

    @Override
    public void setPresenter(final TakePictureContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
