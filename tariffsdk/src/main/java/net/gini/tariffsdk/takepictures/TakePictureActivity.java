package net.gini.tariffsdk.takepictures;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import net.gini.tariffsdk.R;
import net.gini.tariffsdk.TariffSdkBaseActivity;
import net.gini.tariffsdk.camera.Camera1;
import net.gini.tariffsdk.camera.GiniCamera;
import net.gini.tariffsdk.camera.GiniCameraException;
import net.gini.tariffsdk.documentservice.DocumentService;
import net.gini.tariffsdk.documentservice.DocumentServiceImpl;
import net.gini.tariffsdk.reviewpicture.ReviewPictureActivity;

final public class TakePictureActivity extends TariffSdkBaseActivity implements
        TakePictureContract.View {

    private static final int PERMISSIONS_REQUEST_CAMERA = 101;
    private ImageAdapter mAdapter;
    private GiniCamera mCamera;
    private SurfaceView mCameraPreview;
    private RecyclerView mImageRecyclerView;
    private TakePictureContract.Presenter mPresenter;

    @Override
    public void cameraPermissionsDenied() {

    }

    @Override
    public boolean cameraPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void openImageReview(@NonNull final Uri uri) {
        Intent intent = ReviewPictureActivity.newIntent(TakePictureActivity.this, uri);
        startActivity(intent);
    }

    @Override
    public void imageSuccessfullyProcessed(@NonNull final Uri imageUri) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.hideLoadingForImage(imageUri);
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
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_take_picture);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        final DocumentService documentService = DocumentServiceImpl.getInstance(this);
        final TakePicturePresenter presenter = new TakePicturePresenter(this, documentService);
        setPresenter(presenter);

        final ImageButton takePictureButton = (ImageButton) findViewById(R.id.button_take_picture);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mCamera.takePicture(new GiniCamera.JpegCallback() {
                    @Override
                    public void onPictureTaken(@NonNull final byte[] data)
                            throws GiniCameraException {
                        mPresenter.onPictureTaken(data, TakePictureActivity.this);
                    }
                });
            }
        });

        mCameraPreview = (SurfaceView) findViewById(R.id.camera_preview);

        mImageRecyclerView = (RecyclerView) findViewById(R.id.image_overview);
        mImageRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ImageAdapter(new ImageAdapter.Listener() {
            @Override
            public void onImageClicked(final Uri uri) {
                openImageReview(uri);
            }
        });
        mImageRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraPermissionsGranted() && mCamera != null) {
            mCamera.stop();
        }
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
        super.onResume();
        if (cameraPermissionsGranted() && mCamera != null) {
            mCamera.start();
            mCameraPreview.setVisibility(View.VISIBLE);
        }
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
    public void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                PERMISSIONS_REQUEST_CAMERA);
    }

    @Override
    public void setImages(@NonNull final SimpleArrayMap<Uri, Boolean> imageList) {
        mAdapter.setImages(imageList);
    }

    @Override
    public void setPresenter(final TakePictureContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static Intent newIntent(final Context context,
            final int themeResourceId) {


        Intent intent = new Intent(context, TakePictureActivity.class);
        intent.putExtra(BUNDLE_EXTRA_THEME, themeResourceId);
        return intent;
    }
}
