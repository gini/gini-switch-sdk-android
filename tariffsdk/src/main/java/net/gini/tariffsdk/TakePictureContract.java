package net.gini.tariffsdk;


import android.support.annotation.NonNull;

import java.util.List;

interface TakePictureContract {

    interface Presenter {

        void deleteSelectedImage();

        void onAllPicturesTaken();

        void onBoardingFinished();

        void onImageSelected(final Image image);

        void onPictureTaken(@NonNull final byte[] data, final int orientation);

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

        void hideOnboarding();

        void hidePreviewButtons();

        void hideTakePictureButtons();

        void imageStateChanged(@NonNull Image image);

        void initCamera();

        void openImageReview(@NonNull final Image image);

        void openTakePictureScreen();

        void removeImageFromList(@NonNull final Image selectedImage);

        void requestPermissions();

        void setImages(@NonNull final List<Image> imageList);

        void showFoundExtractions();

        void showImagePreview(final Image image);

        void showOnboarding();

        void showPreviewButtons();

        void showTakePictureButtons();
    }
}
