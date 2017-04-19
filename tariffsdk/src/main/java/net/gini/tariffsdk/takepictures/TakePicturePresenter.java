package net.gini.tariffsdk.takepictures;


import static android.support.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.support.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.support.media.ExifInterface.ORIENTATION_ROTATE_90;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.media.ExifInterface;

import net.gini.tariffsdk.documentservice.DocumentService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        final String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(
                new Date());
        final File file = new File(directory, fileName + ".jpeg");
        try {
            final FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.close();

            final ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            final String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            final String newRotation = getNewRotation(orientation);
            exif.setAttribute(ExifInterface.TAG_ORIENTATION, newRotation);
            exif.saveAttributes();

            mView.imageProcessed(Uri.fromFile(file));
        } catch (IOException e) {
            e.printStackTrace();
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
        if (hasToCheckForPermissions() && !mView.cameraPermissionsGranted()) {
            mView.requestPermissions();
        } else {
            mView.initCamera();
        }
    }

    private String getNewRotation(String orientation) {
        switch (orientation) {
            case "3":
                return "" + ORIENTATION_ROTATE_180;
            case "0":
            case "6":
                return "" + ORIENTATION_ROTATE_90;
            case "8":
                return "" + ORIENTATION_ROTATE_270;
        }
        return orientation;
    }

    private boolean hasToCheckForPermissions() {
        return mBuildVersion >= android.os.Build.VERSION_CODES.M;
    }
}
