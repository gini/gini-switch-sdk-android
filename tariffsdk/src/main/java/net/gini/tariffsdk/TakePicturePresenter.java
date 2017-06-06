package net.gini.tariffsdk;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.List;

class TakePicturePresenter implements TakePictureContract.Presenter,
        DocumentService.DocumentListener {

    private final DocumentService mDocumentService;
    private final TakePictureContract.View mView;
    @VisibleForTesting
    int mBuildVersion = android.os.Build.VERSION.SDK_INT;
    private Image mSelectedImage = null;

    TakePicturePresenter(final TakePictureContract.View view,
            final DocumentService documentService) {

        mView = view;
        mDocumentService = documentService;
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
    public void onAllPicturesTaken() {
        mView.showFoundExtractions();
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
    public void onImageStatChanged(@NonNull final Image image) {
        mView.imageStateChanged(image);
    }

    @Override
    public void onPictureTaken(@NonNull final byte[] data) {
        final Image image = mDocumentService.saveImage(data);
        mView.openImageReview(image);
    }

    @Override
    public void onTakePictureSelected() {
        mSelectedImage = null;
        mView.openTakePictureScreen();
        mView.showTakePictureButtons();
        mView.hidePreviewButtons();
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
        if (hasToCheckForPermissions() && !mView.hasCameraPermissions()) {
            mView.requestPermissions();
        } else {
            mView.initCamera();
            setImagesInList();
            mDocumentService.addDocumentListener(this);
        }
    }

    @Override
    public void stop() {
        mDocumentService.removeDocumentListener(this);
    }

    private boolean hasToCheckForPermissions() {
        return mBuildVersion >= android.os.Build.VERSION_CODES.M;
    }

    private void setImagesInList() {
        final List<Image> images = mDocumentService.getImageList();
        mView.setImages(images);
    }
}