package net.gini.tariffsdk.takepictures;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import net.gini.tariffsdk.documentservice.DocumentService;

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
        //TODO
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
        }
    }

    private boolean hasToCheckForPermissions() {
        return mBuildVersion >= android.os.Build.VERSION_CODES.M;
    }
}
