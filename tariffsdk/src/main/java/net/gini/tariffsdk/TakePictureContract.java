package net.gini.tariffsdk;


import android.support.annotation.NonNull;

import java.util.List;

interface TakePictureContract {

    interface Presenter {

        void onAllPicturesTaken();

        void onImageSelected(final Image image);

        void onPictureTaken(@NonNull final byte[] data);

        void onTakePictureSelected();

        void permissionResultDenied();

        void permissionResultGranted();

        void start();

        void stop();

    }

    interface View {

        void cameraPermissionsDenied();

        void displayImageProcessingState(Image image);

        boolean hasCameraPermissions();

        void hidePreviewButtons();

        void hideTakePictureButtons();

        void imageStateChanged(@NonNull Image image);

        void initCamera();

        void openImageReview(@NonNull final Image image);

        void openTakePictureScreen();

        void requestPermissions();

        void setImages(@NonNull final List<Image> imageList);

        void showFoundExtractions();

        void showImagePreview(final Image image);

        void showPreviewButtons();

        void showTakePictureButtons();
    }
}
