package net.gini.tariffsdk;


import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import net.gini.tariffsdk.camera.Camera1;
import net.gini.tariffsdk.camera.GiniCamera;
import net.gini.tariffsdk.camera.GiniCameraException;
import net.gini.tariffsdk.utils.AutoRotateImageView;
import net.gini.tariffsdk.utils.ExitDialogFragment;

import java.util.List;

final public class TakePictureActivity extends TariffSdkBaseActivity implements
        TakePictureContract.View, ExitDialogFragment.ExitDialogListener {

    private static final int PERMISSIONS_REQUEST_CAMERA = 101;
    private static final int REQUEST_CODE_EXTRACTIONS = 123;
    private ImageAdapter mAdapter;
    private GiniCamera mCamera;
    private SurfaceView mCameraPreview;
    private AutoRotateImageView mImagePreview;
    private TakePictureContract.Presenter mPresenter;
    private ProgressBar mProgressBar;
    private ImageButton mTakePictureButton;

    @Override
    public void cameraPermissionsDenied() {

    }

    @Override
    public boolean hasCameraPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void imageStateChanged(@NonNull final Image image) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.updateImageState(image);
            }
        });
    }

    @Override
    public void initCamera() {
        final WindowManager windowManager =
                (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mCamera = new Camera1(mCameraPreview);
        mCamera.setPreviewOrientation(GiniCamera.Orientation.PORTRAIT,
                windowManager.getDefaultDisplay().getRotation());
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        if (resultCode != RESULT_CANCELED && requestCode == REQUEST_CODE_EXTRACTIONS) {
            setResult(resultCode);

            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DialogFragment dialog = new ExitDialogFragment();
        dialog.show(getFragmentManager(), "ExitDialogFragment");
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getCallingActivity() == null) {
            throw new IllegalStateException(
                    "Start this Intent with startActivityForResult() please, otherwise it will "
                            + "not work correctly!");
        }

        setContentView(R.layout.activity_take_picture);


        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        final DocumentService documentService = TariffSdk.getSdk().getDocumentService();
        mPresenter = new TakePicturePresenter(this, documentService);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        mTakePictureButton = (ImageButton) findViewById(R.id.button_take_picture);
        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                mTakePictureButton.setEnabled(false);
                mCamera.takePicture(new GiniCamera.JpegCallback() {
                    @Override
                    public void onPictureTaken(@NonNull final byte[] data)
                            throws GiniCameraException {
                        mPresenter.onPictureTaken(data);
                    }
                });
            }
        });

        findViewById(R.id.button_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mPresenter.onAllPicturesTaken();
            }
        });

        mCameraPreview = (SurfaceView) findViewById(R.id.camera_preview);
        mImagePreview = (AutoRotateImageView) findViewById(R.id.image_review);


        final RecyclerView imageRecyclerView = (RecyclerView) findViewById(R.id.image_overview);
        imageRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ImageAdapter(this, new ImageAdapter.Listener() {
            @Override
            public void onCameraClicked() {
                mPresenter.onTakePictureSelected();
            }

            @Override
            public void onImageClicked(final Image image) {
                mPresenter.onImageSelected(image);
            }
        });
        imageRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onNegative() {
        //TODO track etc.
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hasCameraPermissions() && mCamera != null) {
            mCamera.stop();
            mCameraPreview.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPositive() {
        //TODO track etc.
        TariffSdk.getSdk().cleanUp();
        finishAffinity();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
            @NonNull final String[] permissions,
            @NonNull final int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.permissionResultGranted();
            } else {
                mPresenter.permissionResultDenied();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onResume() {
        if (hasCameraPermissions() && mCamera != null) {
            mCamera.start();
            mCameraPreview.setVisibility(View.VISIBLE);
            mTakePictureButton.setEnabled(true);
            mProgressBar.setVisibility(View.GONE);
        }
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @Override
    public void openImageReview(@NonNull final Image image) {

        final IntentFactory tariffSdkIntentFactory = new IntentFactory(
                TariffSdk.getSdk());
        final Intent intent = tariffSdkIntentFactory.createReviewActivity(image.getUri());
        startActivity(intent);
    }

    @Override
    public void openTakePictureScreen() {
        mCameraPreview.setVisibility(View.VISIBLE);
        mImagePreview.setVisibility(View.GONE);
    }

    @Override
    public void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                PERMISSIONS_REQUEST_CAMERA);
    }

    @Override
    public void setImages(@NonNull final List<Image> imageList) {
        mAdapter.setImages(imageList);
    }

    @Override
    public void showFoundExtractions() {
        IntentFactory intentFactory = new IntentFactory(TariffSdk.getSdk());
        Intent intent = intentFactory.createExtractionsActivity();
        startActivityForResult(intent, REQUEST_CODE_EXTRACTIONS);
    }

    @Override
    public void showImagePreview(final Image image) {
        mImagePreview.displayImage(image.getUri());
        mCameraPreview.setVisibility(View.GONE);
        mImagePreview.setVisibility(View.VISIBLE);
    }
}
