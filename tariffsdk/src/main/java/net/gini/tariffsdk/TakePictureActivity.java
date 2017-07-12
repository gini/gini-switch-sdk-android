package net.gini.tariffsdk;


import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static android.view.View.SCREEN_STATE_ON;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.gini.tariffsdk.camera.Camera1;
import net.gini.tariffsdk.camera.CameraSurfacePreview;
import net.gini.tariffsdk.camera.GiniCamera;
import net.gini.tariffsdk.camera.GiniCameraException;
import net.gini.tariffsdk.onboarding.OnboardingAdapter;
import net.gini.tariffsdk.utils.AutoRotateImageView;
import net.gini.tariffsdk.utils.CenterItemDecoration;
import net.gini.tariffsdk.utils.CenterSnapHelper;

import java.util.List;

final public class TakePictureActivity extends TariffSdkBaseActivity implements
        TakePictureContract.View {

    public static final int ANALYSE_COMPLETE_ANIMATION_DURATION_IN_MS = 250;
    public static final int ANALYSE_COMPLETE_SHOW_DURATION_IN_MS = 3000;
    static final String BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE = "BUNDLE_EXTRA_BUTTON_ANALYZED_IMAGE";
    static final String BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT = "BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT";
    static final String BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR =
            "BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_COLOR";
    static final String BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE =
            "BUNDLE_EXTRA_BUTTON_ANALYZED_TEXT_SIZE";
    static final String BUNDLE_EXTRA_PREVIEW_FAILED_TEXT = "BUNDLE_EXTRA_PREVIEW_FAILED_TEXT";
    static final String BUNDLE_EXTRA_PREVIEW_SUCCESS_TEXT = "BUNDLE_EXTRA_PREVIEW_SUCCESS_TEXT";
    private static final int PERMISSIONS_REQUEST_CAMERA = 101;
    private ImageAdapter mAdapter;
    private GiniCamera mCamera;
    private View mCameraFrame;
    private CameraSurfacePreview mCameraPreview;
    private TextView mImageNumberText;
    private AutoRotateImageView mImagePreview;
    private ImageView mImagePreviewState;
    private RecyclerView mImageRecyclerView;
    private View mOnboardingContainer;
    private TakePictureContract.Presenter mPresenter;
    private View mPreviewButtonsContainer;
    private TextView mPreviewTitle;
    private ProgressBar mProgressBar;
    private Image mSelectedImage;
    private View mSplashContainer;
    private ImageButton mTakePictureButton;
    private View mTakePictureButtonsContainer;
    private Toolbar mToolbar;

    @Override
    public void cameraPermissionsDenied() {

    }

    @Override
    public void displayImageProcessingState(final Image image) {
        final Drawable drawable;
        if (image.getProcessingState() != ImageState.PROCESSING) {
            if (image.getProcessingState() == ImageState.SUCCESSFULLY_PROCESSED) {
                drawable = ContextCompat.getDrawable(this, R.drawable.ic_check);
                mPreviewTitle.setText(getAnalyzeSuccessTextFromBundle());
            } else {
                drawable = ContextCompat.getDrawable(this, R.drawable.ic_cross);
                mPreviewTitle.setText(getAnalyzeFailedTextFromBundle());
            }
            final int processingColor =
                    (image.getProcessingState() == ImageState.SUCCESSFULLY_PROCESSED)
                            ? ContextCompat.getColor(this, getPositiveColor())
                            : ContextCompat.getColor(this, getNegativeColor());
            drawable.setAlpha(255);
            drawable.setColorFilter(
                    new PorterDuffColorFilter(processingColor, PorterDuff.Mode.SRC_IN));
            mImagePreviewState.setImageDrawable(drawable);
            mImagePreviewState.setVisibility(View.VISIBLE);
        } else {
            mImagePreviewState.setVisibility(View.GONE);
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
    public void hideImageNumberTitle() {
        mImageNumberText.setText(null);
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
                if (mSelectedImage == image) {
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

        final TariffSdk tariffSdk = TariffSdk.getSdk();
        final DocumentService documentService = tariffSdk.getDocumentService();
        final ExtractionService extractionService = tariffSdk.getExtractionService();
        mPresenter = new TakePicturePresenter(this, documentService, extractionService,
                new OnboardingManager(this));

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
        mImagePreview = (AutoRotateImageView) findViewById(R.id.image_review);
        mImagePreviewState = (ImageView) findViewById(R.id.image_state);
        mPreviewTitle = (TextView) findViewById(R.id.analyzed_status_title);

        setUpDocumentBar();

        mSplashContainer = findViewById(R.id.container_splash);
        setUpAnalyzedCompletedScreen();

        mTakePictureButtonsContainer = findViewById(R.id.container_take_picture_buttons);
        mPreviewButtonsContainer = findViewById(R.id.container_preview_buttons);


        ImageButton deleteImageButton = (ImageButton) findViewById(R.id.button_delete_image);
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
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuItem menuItem = menu.add(R.string.menu_entry_help);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                if (item.getItemId() == menuItem.getItemId()) {
                    showOnboarding();
                    return false;
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hasCameraPermissions() && mCamera != null) {
            mCamera.stop();
            hideCameraPreview();
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
        if (hasCameraPermissions() && mCamera != null) {
            mCamera.start();
            showCameraPreview();
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
        mSelectedImage = null;
        showCameraPreview();
        mImagePreview.setVisibility(View.GONE);
        mImagePreviewState.setVisibility(View.GONE);
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
        mImageRecyclerView.scrollToPosition(mAdapter.getLastPosition());
    }

    @Override
    public void showImageNumberTitle(final int imageNumber) {
        //Hardcoded because I hope we will remove this
        mImageNumberText.setText("Foto " + imageNumber);
    }

    @Override
    public void showImagePreview(final Image image) {
        mSelectedImage = image;
        mImagePreview.displayImage(image.getUri());
        hideCameraPreview();
        mImagePreview.setVisibility(View.VISIBLE);
        //TODO save this state and retrieve it
    }

    @Override
    public void showOnboarding() {
        mOnboardingContainer.setVisibility(View.VISIBLE);
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
        mImageNumberText = (TextView) mToolbar.findViewById(R.id.image_number_text);
        mImageRecyclerView = (RecyclerView) mToolbar.findViewById(R.id.image_overview);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false);
        mImageRecyclerView.setLayoutManager(
                layoutManager);
        final CenterSnapHelper centerSnapHelper = new CenterSnapHelper();
        centerSnapHelper.attachToRecyclerView(mImageRecyclerView);
        mImageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                if (newState == SCROLL_STATE_IDLE) {
                    final int position = centerSnapHelper.getCenteredPosition();
                    if (position < mAdapter.getLastPosition()) {
                        final Image item = mAdapter.getItem(position);
                        mPresenter.onImageSelected(item, position + 1);
                        mAdapter.showPlus();
                    } else {
                        mPresenter.onTakePictureSelected();
                        mAdapter.hidePlus();
                    }
                }
                if (newState == SCREEN_STATE_ON) {
                    mAdapter.showPlus();
                    hideImageNumberTitle();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        mAdapter = new ImageAdapter(this, new ImageAdapter.Listener() {
            @Override
            public void onCameraClicked() {
                mPresenter.onTakePictureSelected();
                layoutManager.scrollToPosition(mAdapter.getLastPosition());
            }

            @Override
            public void onImageClicked(final Image image, final int position) {
                mPresenter.onImageSelected(image, position + 1);
                layoutManager.scrollToPosition(position);
            }

        }, getPositiveColor(), getNegativeColor());

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
        final FloatingActionButton onboardingNextButton = (FloatingActionButton) findViewById(
                R.id.button_onboarding_next);
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
                }
            }
        });
        onboardingNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                int nextItem = onboardingViewPager.getCurrentItem() + 1;
                onboardingViewPager.setCurrentItem(nextItem, true);
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
        mCameraPreview.setVisibility(View.VISIBLE);
        mCameraFrame.setVisibility(View.VISIBLE);
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
