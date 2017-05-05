package net.gini.tariffsdk;


import android.support.annotation.NonNull;

import java.util.List;

interface TakePictureContract {

    interface Presenter {

        void onAllPicturesTaken();

        void onPictureTaken(@NonNull final byte[] data);

        void permissionResultDenied();

        void permissionResultGranted();

        void start();

        void stop();

    }

    interface View {

        void cameraPermissionsDenied();

        boolean hasCameraPermissions();

        void imageStateChanged(@NonNull Image image);

        void initCamera();

        void openImageReview(@NonNull final Image image);

        void requestPermissions();

        void setImages(@NonNull final List<Image> imageList);

        void showFoundExtractions();
    }
}
