package net.gini.tariffsdk;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.List;

class TakePicturePresenter implements TakePictureContract.Presenter,
        DocumentService.DocumentListener {

    private final DocumentService mDocumentService;
    private final TakePictureContract.View mView;
    @VisibleForTesting
    int mBuildVersion = android.os.Build.VERSION.SDK_INT;

    TakePicturePresenter(final TakePictureContract.View view,
            final DocumentService documentService) {

        mView = view;
        mDocumentService = documentService;
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
    public void permissionResultDenied() {
        mView.cameraPermissionsDenied();
    }

    @Override
    public void permissionResultGranted() {
        mView.initCamera();
    }

    @Override
    public void start() {
        if (hasToCheckForPermissions() && !mView.cameraPermissionsGranted()) {
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
