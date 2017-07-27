package net.gini.switchsdk;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.util.List;

class TakePicturePresenter implements TakePictureContract.Presenter,
        DocumentService.DocumentListener {

    private final DocumentService mDocumentService;
    private final ExtractionService mExtractionService;
    private final OnboardingManager mOnboardingManager;
    private final TakePictureContract.View mView;
    @VisibleForTesting
    int mBuildVersion = android.os.Build.VERSION.SDK_INT;
    private Image mSelectedImage = null;

    TakePicturePresenter(final TakePictureContract.View view,
            final DocumentService documentService,
            final ExtractionService extractionService,
            final OnboardingManager onboardingManager) {

        mView = view;
        mDocumentService = documentService;
        mExtractionService = extractionService;
        mOnboardingManager = onboardingManager;
        mDocumentService.createExtractionOrder();
    }

    @Override
    public void deleteSelectedImage() {
        if (mSelectedImage != null) {
            final Uri uri = mSelectedImage.getUri();
            mDocumentService.deleteImage(uri);
            mView.removeImageFromList(mSelectedImage);
            mView.openTakePictureScreen();
            mSelectedImage = null;
        }
        mView.showTakePictureButtons();
    }

    @Nullable
    @Override
    public Image getSelectedImage() {
        return mSelectedImage;
    }

    @Override
    public void onBoardingFinished() {
        mView.hideOnboarding();
        mOnboardingManager.storeOnboardingShown();
    }

    @Override
    public void onFinishedClicked() {
        String extractionUrl = mDocumentService.getExtractionUrl();
        if (extractionUrl != null) {
            mExtractionService.fetchExtractions(extractionUrl,
                    new ExtractionService.ExtractionListener() {
                        @Override
                        public void onExtractionsReceived() {
                            //Check if the user is in the camera screen
                            final int resultCode = mExtractionService.getResultCodeForActivity();
                            exitSdkWithCleanup(resultCode);
                        }
                    });
        } else {
            int resultCode = SwitchSdk.NO_EXTRACTIONS_AVAILABLE;
            exitSdkWithCleanup(resultCode);
        }
    }

    @Override
    public void onImageSelected(final Image image) {
        mSelectedImage = image;
        mView.showImagePreview(image);
        mView.displayImageProcessingState(image);
        mView.showPreviewButtons();
        mView.hideTakePictureButtons();
    }

    @Override
    public void onImageStateChanged(@NonNull final Image image) {
        mView.imageStateChanged(image);
    }

    @Override
    public void onOrderCompleted(@NonNull final String extractionUrl) {
        mExtractionService.fetchExtractions(extractionUrl,
                new ExtractionService.ExtractionListener() {
                    @Override
                    public void onExtractionsReceived() {
                        //Check if the user is in the camera screen
                        if (canExitSdk()) {
                            int resultCode = SwitchSdk.EXTRACTIONS_AVAILABLE;
                            exitSdkWithCleanup(resultCode);
                        }
                    }
                });
    }

    @Override
    public void onPictureTaken(@NonNull final byte[] data, final int orientation) {
        final Image image = mDocumentService.saveImage(data, orientation);
        mView.openImageReview(image);
    }

    @Override
    public void onTakePictureSelected() {
        if (mView.hasCameraPermissions()) {
            mSelectedImage = null;
            mView.openTakePictureScreen();
            mView.showTakePictureButtons();
            mView.hidePreviewButtons();
            //if there are extractions available we finish the sdk
            if (mExtractionService.extractionsAvailable()) {
                //TODO maybe add delay here
                int resultCode = SwitchSdk.EXTRACTIONS_AVAILABLE;
                exitSdkWithCleanup(resultCode);
            }
        }
    }

    @Override
    public void permissionResultDenied() {
        mView.cameraPermissionsDenied();
    }

    @Override
    public void permissionResultGranted() {
        mView.initCamera();
    }

    @Override
    public void start() {
        if (mOnboardingManager.onBoardingShown()) {
            mView.hideOnboarding();
        } else {
            mView.showOnboardingWithDelayedAnimation();
        }
        startCameraProcess();
    }

    @Override
    public void stop() {
        mDocumentService.removeDocumentListener(this);
    }

    private boolean canExitSdk() {
        return mSelectedImage == null;
    }

    private void exitSdkWithCleanup(final int resultCode) {
        mDocumentService.cleanup();
        mView.exitSdk(resultCode);
    }

    private boolean hasToCheckForPermissions() {
        return mBuildVersion >= android.os.Build.VERSION_CODES.M;
    }

    private void setImagesInList() {
        final List<Image> images = mDocumentService.getImageList();
        mView.setImages(images);
    }

    private void startCameraProcess() {
        if (hasToCheckForPermissions() && !mView.hasCameraPermissions()) {
            mView.requestPermissions();
        } else {
            mView.initCamera();
            setImagesInList();
            mDocumentService.addDocumentListener(this);
        }
    }
}
