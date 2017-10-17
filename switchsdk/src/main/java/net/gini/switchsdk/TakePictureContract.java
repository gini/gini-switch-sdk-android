package net.gini.switchsdk;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

interface TakePictureContract {

    interface Presenter {

        void deleteSelectedImage();

        @Nullable
        Image getSelectedImage();

        void onBoardingFinished();

        void onFinishedClicked();

        void onImageSelected(final Image image);

        void onPictureKept();

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

        void exitSdk(final int resultCode);

        /**
         * Exits the SDK without showing the "your document has been analyzed" screen.
         *
         * @param resultCode the activity's result code
         */
        void exitSdkWithoutAnalyzedScreen(final int resultCode);

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

        void showImagePreview(final Image image);

        void showOnboardingWithDelayedAnimation();

        void showPreviewButtons();

        void showTakePictureButtons();
    }
}
