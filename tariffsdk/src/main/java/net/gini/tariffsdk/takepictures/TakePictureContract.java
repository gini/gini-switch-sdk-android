package net.gini.tariffsdk.takepictures;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

interface TakePictureContract {

    interface Presenter {

        void onPictureTaken(@NonNull final byte[] data);

        void permissionResultDenied();

        void permissionResultGranted();

        void start();

        void stop();

    }

    interface View {

        void cameraPermissionsDenied();

        boolean cameraPermissionsGranted();

        void openImageReview(@NonNull final Uri uri);

        void imageSuccessfullyProcessed(@NonNull final Uri imageUri);

        void initCamera();

        void requestPermissions();

        void setImages(@NonNull final SimpleArrayMap<Uri, Boolean> imageList);
    }
}
