package net.gini.tariffsdk;


import android.net.Uri;

interface ReviewPictureContract {
    interface Presenter {
        void discardImage();

        void keepImage();

        void rotateImage();
    }

    interface View {

        void finishReview();

        void rotateView();

        void setImage(final Uri uri);
    }
}
