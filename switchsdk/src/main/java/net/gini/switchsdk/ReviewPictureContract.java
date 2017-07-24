package net.gini.switchsdk;


import android.net.Uri;

interface ReviewPictureContract {
    interface Presenter {
        void discardImage();

        int getRotation();

        void setRotation(int rotation);

        void keepImage();

        void rotateImage();
    }

    interface View {

        void finishReview();

        void rotateViewAnimated();

        void setImage(final Uri uri);
    }
}
