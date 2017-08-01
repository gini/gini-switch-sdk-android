package net.gini.switchsdk;


import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.util.TypedValue.COMPLEX_UNIT_SP;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.gini.switchsdk.camera.Camera1;
import net.gini.switchsdk.camera.CameraSurfacePreview;
import net.gini.switchsdk.camera.GiniCamera;
import net.gini.switchsdk.camera.GiniCameraException;
import net.gini.switchsdk.onboarding.OnboardingAdapter;
import net.gini.switchsdk.utils.AutoRotateZoomableImageView;
import net.gini.switchsdk.utils.CenterItemDecoration;
import net.gini.switchsdk.utils.CenterLayoutManager;
import net.gini.switchsdk.utils.CenterSnapHelper;

import java.util.List;

final public class TakePictureActivity extends SwitchSdkBaseActivity implements
        TakePictureContract.View {

    public static final int ANALYSE_COMPLETE_ANIMATION_DURATION_IN_MS = 250;
    public static final int ANALYSE_COMPLETE_SHOW_DURATION_IN_MS = 4000;
    static final String BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE = "BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE";
    static final String BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT = "BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT";
    static final String BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR =
            "BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR";
    static final String BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE =
            "BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE";
    static final String BUNDLE_EXTRA_PREVIEW_FAILED_TEXT = "BUNDLE_EXTRA_PREVIEW_FAILED_TEXT";
    static final String BUNDLE_EXTRA_PREVIEW_SUCCESS_TEXT = "BUNDLE_EXTRA_PREVIEW_SUCCESS_TEXT";
    private static final long ONBOARDING_ANIMATION_DELAY_IN_MS = 1500;
    private static final long ONBOARDING_ANIMATION_DURATION_IN_MS = 500;
    private static final int PERMISSIONS_REQUEST_CAMERA = 101;
    private static final String STATE_KEY_SELECTED_IMAGE = "STATE_KEY_SELECTED_IMAGE";
    private ImageAdapter mAdapter;
    private GiniCamera mCamera;
    private View mCameraFrame;
    private CameraSurfacePreview mCameraPreview;
    private AutoRotateZoomableImageView mImagePreview;
    private View mImagePreviewContainer;
    private RecyclerView mImageRecyclerView;
    private View mOnboardingContainer;
    private TakePictureContract.Presenter mPresenter;
    private View mPreviewButtonsContainer;
    private TextView mPreviewTitle;
    private ProgressBar mProgressBar;
    private View mSplashContainer;
    private ImageButton mTakePictureButton;
    private View mTakePictureButtonsContainer;
    private Toolbar mToolbar;

    @Override
    public void cameraPermissionsDenied() {
        Snackbar.make(mTakePictureButtonsContainer,
                getString(R.string.snackbar_text_no_camera_access,
                        getApplicationInfo().loadLabel(getPackageManager())),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.snackbar_action_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    public void displayImageProcessingState(final Image image) {
        if (image.getProcessingState() != ImageState.PROCESSING) {
            if (image.getProcessingState() == ImageState.SUCCESSFULLY_PROCESSED) {
                mPreviewTitle.setText(getAnalyzeSuccessTextFromBundle());
            } else {
                mPreviewTitle.setText(getAnalyzeFailedTextFromBundle());
            }
        } else {
            mPreviewTitle.setText(null);
        }

    }

    @Override
    public void exitSdk(final int resultCode) {
        setResult(resultCode);

        showAnalyzedCompletedScreen(new ViewPropertyAnimatorListener() {
            @Override
            public void onAnimationCancel(final View view) {
            }

            @Override
            public void onAnimationEnd(final View view) {
                finish();
            }

            @Override
            public void onAnimationStart(final View view) {
                mTakePictureButtonsContainer.setVisibility(View.GONE);
                mCameraPreview.setVisibility(View.GONE);
                mCameraFrame.setVisibility(View.GONE);
                mToolbar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public boolean hasCameraPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void hideOnboarding() {
        mOnboardingContainer.setVisibility(View.GONE);
    }

    @Override
    public void hidePreviewButtons() {
        mPreviewButtonsContainer.setVisibility(View.GONE);
    }

    @Override
    public void hideTakePictureButtons() {
        mTakePictureButtonsContainer.setVisibility(View.GONE);
    }

    @Override
    public void imageStateChanged(@NonNull final Image image) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.updateImageState(image);
                if (mPresenter.getSelectedImage() == image) {
                    displayImageProcessingState(image);
                }
            }
        });
    }

    @Override
    public void initCamera() {
        final WindowManager windowManager =
                (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mCamera = new Camera1(mCameraPreview);
        mCamera.setPreviewOrientation(windowManager.getDefaultDisplay().getRotation());
    }

    @Override
    public void onBackPressed() {
        showAbortDialog();
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

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        colorToolbar(mToolbar);

        setUpOnboarding();

        final SwitchSdk switchSdk = SwitchSdk.getSdk();
        final DocumentService documentService = switchSdk.getDocumentService();
        final ExtractionService extractionService = switchSdk.getExtractionService();
        mPresenter = new TakePicturePresenter(this, documentService, extractionService,
                new OnboardingManager(this));

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        mTakePictureButton = (ImageButton) findViewById(R.id.button_take_picture);

        Drawable cameraIcon = ContextCompat.getDrawable(this, android.R.drawable.ic_menu_camera);
        ColorFilter filter = new LightingColorFilter(Color.WHITE, Color.WHITE);
        cameraIcon.setColorFilter(filter);
        mTakePictureButton.setImageDrawable(cameraIcon);
        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                mTakePictureButton.setEnabled(false);
                mCamera.takePicture(new GiniCamera.JpegCallback() {
                    @Override
                    public void onPictureTaken(@NonNull final byte[] data, final int orientation)
                            throws GiniCameraException {
                        mPresenter.onPictureTaken(data, orientation);
                    }
                });
            }
        });

        final Button finishButton = (Button) findViewById(R.id.button_finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mPresenter.onFinishedClicked();
            }
        });

        mCameraPreview = (CameraSurfacePreview) findViewById(R.id.camera_preview);
        mCameraFrame = findViewById(R.id.camera_frame);
        mImagePreview = (AutoRotateZoomableImageView) findViewById(R.id.image_review);
        mImagePreviewContainer = findViewById(R.id.container_image);
        mPreviewTitle = (TextView) findViewById(R.id.analyzed_status_title);

        setUpDocumentBar();

        mSplashContainer = findViewById(R.id.container_splash);
        setUpAnalyzedCompletedScreen();

        mTakePictureButtonsContainer = findViewById(R.id.container_take_picture_buttons);
        mPreviewButtonsContainer = findViewById(R.id.container_preview_buttons);


        ImageButton deleteImageButton = (ImageButton) findViewById(R.id.button_delete_image);
        Drawable deleteIcon = ContextCompat.getDrawable(this, android.R.drawable.ic_menu_delete);
        deleteIcon.setColorFilter(filter);
        deleteImageButton.setImageDrawable(deleteIcon);
        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mPresenter.deleteSelectedImage();
            }
        });
        Button retakeImageButton = (Button) findViewById(R.id.button_take_new_image);
        retakeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //TODO implement retake logic
                mPresenter.deleteSelectedImage();
                mImageRecyclerView.scrollToPosition(mAdapter.getLastPosition());
            }
        });

        styleButtons(finishButton, deleteImageButton, retakeImageButton);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hasCameraPermissions() && mCamera != null) {
            mCamera.stop();
            hideCameraPreview();
        }
        getIntent().putExtra(STATE_KEY_SELECTED_IMAGE, mPresenter.getSelectedImage());
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
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            final Image selectedImage = savedInstanceState.getParcelable(STATE_KEY_SELECTED_IMAGE);
            getIntent().putExtra(STATE_KEY_SELECTED_IMAGE, selectedImage);
        }
    }

    @Override
    protected void onResume() {
        if (hasCameraPermissions() && mCamera != null) {
            mCamera.start();
            mProgressBar.setVisibility(View.GONE);
            mTakePictureButton.setEnabled(true);
        }

        final Image selectedImage = getIntent().getParcelableExtra(STATE_KEY_SELECTED_IMAGE);
        if (selectedImage != null) {
            mPresenter.onImageSelected(selectedImage);
        } else {
            mPresenter.onTakePictureSelected();
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_KEY_SELECTED_IMAGE, mPresenter.getSelectedImage());
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

        final IntentFactory switchSdkIntentFactory = new IntentFactory(
                SwitchSdk.getSdk());
        final Intent intent = switchSdkIntentFactory.createReviewActivity(image.getUri());
        startActivity(intent);
    }

    @Override
    public void openTakePictureScreen() {
        mImagePreviewContainer.setVisibility(View.GONE);

        final int lastItem = mAdapter.getLastPosition();
        mImageRecyclerView.smoothScrollToPosition(lastItem);
        mAdapter.setSelectedElement(lastItem);
        showCameraPreview();
    }

    @Override
    public void removeImageFromList(@NonNull final Image selectedImage) {
        mAdapter.deleteImage(selectedImage);
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
    protected void showHelpDialog() {
        mOnboardingContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showImagePreview(final Image image) {
        mImagePreview.displayImage(image.getUri());
        hideCameraPreview();
        mImagePreviewContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOnboardingWithDelayedAnimation() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int height = size.y;
        mOnboardingContainer.setVisibility(View.VISIBLE);
        mOnboardingContainer.setTranslationY(height);
        ViewCompat.animate(mOnboardingContainer)
                .translationY(0)
                .setStartDelay(ONBOARDING_ANIMATION_DELAY_IN_MS)
                .setDuration(ONBOARDING_ANIMATION_DURATION_IN_MS)
                .start();
    }

    @Override
    public void showPreviewButtons() {
        mPreviewButtonsContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTakePictureButtons() {
        mTakePictureButtonsContainer.setVisibility(View.VISIBLE);
    }

    private int getAnalyzeFailedTextFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_PREVIEW_FAILED_TEXT,
                R.string.preview_analyze_failed);
    }

    private int getAnalyzeSuccessTextFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_PREVIEW_SUCCESS_TEXT,
                R.string.preview_analyze_success);
    }

    private int getAnalyzedImageFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE,
                R.drawable.ic_check_circle);
    }

    private int getAnalyzedTextColorFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR,
                R.color.primaryText);
    }

    private int getAnalyzedTextFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT, R.string.analyzed_text);
    }

    private int getAnalyzedTextSizeFromBundle() {
        return getIntent().getIntExtra(BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE,
                getResources().getInteger(R.integer.analyzed_text_size));
    }

    private void hideCameraPreview() {
        mCameraPreview.setVisibility(View.GONE);
        mCameraFrame.setVisibility(View.GONE);
    }

    private void setUpAnalyzedCompletedScreen() {
        ImageView analyzedImage = (ImageView) findViewById(R.id.analyzed_image);
        analyzedImage.setImageDrawable(ContextCompat.getDrawable(this, getAnalyzedImageFromBundle
                ()));

        TextView analyzedText = (TextView) findViewById(R.id.analyzed_text);
        analyzedText.setText(getAnalyzedTextFromBundle());
        analyzedText.setTextColor(ContextCompat.getColor(this, getAnalyzedTextColorFromBundle()));
        analyzedText.setTextSize(COMPLEX_UNIT_SP, getAnalyzedTextSizeFromBundle());
    }

    private void setUpDocumentBar() {
        mImageRecyclerView = (RecyclerView) mToolbar.findViewById(R.id.image_overview);

        final CenterSnapHelper centerSnapHelper = new CenterSnapHelper();
        centerSnapHelper.attachToRecyclerView(mImageRecyclerView);


        CenterLayoutManager layoutManager = new CenterLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mImageRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new ImageAdapter(this, new ImageAdapter.Listener() {
            @Override
            public void onCameraClicked() {
                mImageRecyclerView.smoothScrollToPosition(mAdapter.getLastPosition());
                centerSnapHelper.setCenteredPosition(mAdapter.getLastPosition());
            }

            @Override
            public void onImageClicked(final Image image, final int position) {
                mImageRecyclerView.smoothScrollToPosition(position);
                centerSnapHelper.setCenteredPosition(position);
            }

        }, getPositiveColor(), getNegativeColor());

        mImageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int oldPosition = -1;

            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                if (newState == SCROLL_STATE_IDLE) {
                    final int position = centerSnapHelper.getCenteredPosition();
                    if (position != oldPosition) {
                        oldPosition = position;
                        mAdapter.setSelectedElement(position);
                        if (position < mAdapter.getLastPosition()) {
                            final Image item = mAdapter.getItem(position);
                            mPresenter.onImageSelected(item);
                        } else {
                            mPresenter.onTakePictureSelected();
                        }
                    }
                }
            }
        });
        mImageRecyclerView.setAdapter(mAdapter);
        mImageRecyclerView.addItemDecoration(new CenterItemDecoration());
        mImageRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mImageRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        mImageRecyclerView.invalidateItemDecorations();
                    }
                });

    }

    private void setUpOnboarding() {
        mOnboardingContainer = findViewById(R.id.onBoardingContainer);
        final ViewPager onboardingViewPager = (ViewPager) findViewById(R.id.onBoardingViewPager);
        final OnboardingAdapter adapter = new OnboardingAdapter(this);
        onboardingViewPager.setAdapter(adapter);
        final TabLayout onboardingTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        onboardingTabLayout.setupWithViewPager(onboardingViewPager, true);
        onboardingViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int state) {
            }

            @Override
            public void onPageScrolled(final int position, final float positionOffset,
                    final int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                if (adapter.isLastItem(position)) {
                    mPresenter.onBoardingFinished();
                    onboardingViewPager.setCurrentItem(0, false);
                }
            }
        });
    }

    private void showAnalyzedCompletedScreen(final ViewPropertyAnimatorListener animatorListener) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int height = size.y;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSplashContainer.setVisibility(View.VISIBLE);
                mSplashContainer.setTranslationY(height);
                ViewCompat.animate(mSplashContainer)
                        .translationY(0)
                        .setDuration(ANALYSE_COMPLETE_ANIMATION_DURATION_IN_MS)
                        .setListener(new ViewPropertyAnimatorListener() {
                            @Override
                            public void onAnimationCancel(final View view) {
                            }

                            @Override
                            public void onAnimationEnd(final View view) {
                                ViewCompat.animate(view)
                                        .translationY(height)
                                        .setDuration(ANALYSE_COMPLETE_ANIMATION_DURATION_IN_MS)
                                        .setStartDelay(ANALYSE_COMPLETE_SHOW_DURATION_IN_MS)
                                        .setListener(animatorListener)
                                        .start();
                            }

                            @Override
                            public void onAnimationStart(final View view) {
                            }
                        })
                        .start();
            }
        });
    }

    private void showCameraPreview() {
        mCameraPreview.post(new Runnable() {
            @Override
            public void run() {
                mCameraPreview.setVisibility(View.VISIBLE);
                mCameraFrame.setVisibility(View.VISIBLE);
            }
        });
    }

    private void styleButtons(final Button finishButton, final ImageButton deleteImageButton,
            final Button retakeImageButton) {
        if (hasCustomButtonStyleSet()) {
            int customButtonStyle = getButtonStyleResourceIdFromBundle();
            deleteImageButton.setBackgroundResource(customButtonStyle);
            retakeImageButton.setBackgroundResource(customButtonStyle);
            finishButton.setBackgroundResource(customButtonStyle);
        } else {
            ViewCompat.setBackgroundTintList(retakeImageButton,
                    ContextCompat.getColorStateList(this, R.color.positiveColor));
            ViewCompat.setBackgroundTintList(deleteImageButton,
                    ContextCompat.getColorStateList(this, R.color.negativeColor));
            ViewCompat.setBackgroundTintList(finishButton,
                    ContextCompat.getColorStateList(this, R.color.primaryColor));
        }

        if (hasCustomButtonTextColor()) {
            int customButtonTextColor = getButtonTextColorResourceIdFromBundle();
            int textColor = ContextCompat.getColor(this, customButtonTextColor);
            retakeImageButton.setTextColor(textColor);
            finishButton.setTextColor(textColor);
        } else {
            finishButton.setTextColor(getAccentColor());
        }
    }
}
