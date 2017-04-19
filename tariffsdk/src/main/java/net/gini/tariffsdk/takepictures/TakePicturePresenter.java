package net.gini.tariffsdk.takepictures;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.SimpleArrayMap;

import net.gini.tariffsdk.documentservice.DocumentService;

import java.io.File;

class TakePicturePresenter implements TakePictureContract.Presenter {

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
    public void onPictureTaken(@NonNull final byte[] data, @NonNull final Context context) {
        final File directory = context.getDir("tariffsdk", Context.MODE_PRIVATE);
        final Uri uri = mDocumentService.saveImage(data, directory);
        mView.imageProcessed(uri);
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
        }
    }

    private void setImagesInList() {
        final SimpleArrayMap<Uri, Boolean> images = mDocumentService.getImageList();
        mView.setImages(images);
    }

    private boolean hasToCheckForPermissions() {
        return mBuildVersion >= android.os.Build.VERSION_CODES.M;
    }
}
