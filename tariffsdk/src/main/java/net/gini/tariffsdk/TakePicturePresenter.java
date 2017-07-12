package net.gini.tariffsdk;


import android.net.Uri;
import android.support.annotation.NonNull;
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
                            mView.exitSdk(resultCode);
                        }
                    });
        } else {
            mView.exitSdk(TariffSdk.NO_EXTRACTIONS_AVAILABLE);
        }
    }

    @Override
    public void onImageSelected(final Image image, final int imageNumber) {
        mSelectedImage = image;
        mView.showImagePreview(image);
        mView.displayImageProcessingState(image);
        mView.showPreviewButtons();
        mView.hideTakePictureButtons();
        mView.showImageNumberTitle(imageNumber);
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
                            mView.exitSdk(TariffSdk.EXTRACTIONS_AVAILABLE);
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
        mSelectedImage = null;
        mView.openTakePictureScreen();
        mView.showTakePictureButtons();
        mView.hidePreviewButtons();
        mView.hideImageNumberTitle();
        //if there are extractions available we finish the sdk
        if (mExtractionService.extractionsAvailable()) {
            //TODO maybe add delay here
            mView.exitSdk(TariffSdk.EXTRACTIONS_AVAILABLE);
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
            mView.showOnboarding();
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
