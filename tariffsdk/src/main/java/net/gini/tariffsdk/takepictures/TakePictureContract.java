package net.gini.tariffsdk.takepictures;


import android.content.Context;
import android.support.annotation.NonNull;

import net.gini.tariffsdk.base.BaseView;

interface TakePictureContract {

    interface Presenter {

        void onPictureTaken(@NonNull final byte[] data, @NonNull final Context context);

        void permissionResultDenied();

        void permissionResultGranted();

        void start();

    }

    interface View extends BaseView<Presenter> {

        void cameraPermissionsDenied();

        boolean cameraPermissionsGranted();

        void initCamera();

        void requestPermissions();
    }
}
