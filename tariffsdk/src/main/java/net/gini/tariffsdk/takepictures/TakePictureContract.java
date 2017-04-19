package net.gini.tariffsdk.takepictures;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import net.gini.tariffsdk.base.BaseView;

interface TakePictureContract {

    interface Presenter {

        void onPictureTaken(@NonNull final byte[] data, @NonNull final Context context);

        void permissionResultDenied();

        void permissionResultGranted();

        void start();

        void stop();

    }

    interface View extends BaseView<Presenter> {

        void cameraPermissionsDenied();

        boolean cameraPermissionsGranted();

        void openImageReview(@NonNull final Uri uri);

        void imageSuccessfullyProcessed(@NonNull final Uri imageUri);

        void initCamera();

        void requestPermissions();

        void setImages(@NonNull final SimpleArrayMap<Uri, Boolean> imageList);
    }
}
